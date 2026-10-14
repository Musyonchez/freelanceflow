package com.daleen.freelanceflow.report

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.Expense
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.presentation.screens.home.ReportPeriod
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToLong

/**
 * Builds a single CSV covering invoices and expenses for a date range, each
 * section with its own totals converted into the user's base currency --
 * per FR-8.3 and the reporting success metric in docs/03.
 */
class ReportGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun generate(
        period: ReportPeriod,
        baseCurrency: String,
        invoices: List<Invoice>,
        expenses: List<Expense>,
        clients: List<Client>,
        projects: List<Project>,
        rates: Map<String, Double>,
        deductionRatePercent: Double?,
    ): File {
        val clientNameById = clients.associate { it.id to it.name }
        val projectNameById = projects.associate { it.id to it.name }
        val csv = StringBuilder()

        fun row(vararg cells: Any) {
            csv.append(cells.joinToString(",") { escapeCsv(it.toString()) }).append("\n")
        }

        row("FreelanceFlow report", period.label, "Generated ${dateFormat.format(Date())}")
        csv.append("\n")

        row("Invoices")
        row("Invoice #", "Client", "Issue date", "Due date", "Status", "Subtotal", "Total", "Currency")
        invoices.forEach { invoice ->
            row(
                invoice.invoiceNumber,
                clientNameById[invoice.clientId] ?: "Unknown client",
                dateFormat.format(Date(invoice.issueDate)),
                dateFormat.format(Date(invoice.dueDate)),
                invoice.status.name,
                formatMajor(invoice.subtotal.minorUnits),
                formatMajor(invoice.total.minorUnits),
                invoice.billingCurrency,
            )
        }
        val outstanding = invoices.filter { it.status != InvoiceStatus.PAID }
        val paid = invoices.filter { it.status == InvoiceStatus.PAID }
        row("Total outstanding (in $baseCurrency)", formatMajor(convertedMinor(outstanding.map { it.total }, rates)))
        row("Total paid (in $baseCurrency)", formatMajor(convertedMinor(paid.map { it.total }, rates)))
        csv.append("\n")

        row("Expenses")
        row("Date", "Category", "Client", "Project", "Amount", "Currency", "Deductible", "Deductible %")
        expenses.forEach { expense ->
            row(
                dateFormat.format(Date(expense.date)),
                expense.category,
                expense.clientId?.let { clientNameById[it] } ?: "",
                expense.projectId?.let { projectNameById[it] } ?: "",
                formatMajor(expense.amount.minorUnits),
                expense.amount.currencyCode,
                if (expense.deductible) "Yes" else "No",
                expense.deductiblePercent,
            )
        }
        row("Total expenses (in $baseCurrency)", formatMajor(convertedMinor(expenses.map { it.amount }, rates)))
        val deductibleMinor = convertedDeductibleMinor(expenses.filter { it.deductible }, rates)
        row("Deductible total (in $baseCurrency)", formatMajor(deductibleMinor))
        if (deductionRatePercent != null) {
            val estimatedMinor = (deductibleMinor * (deductionRatePercent / 100.0)).roundToLong()
            row("Estimated deduction (in $baseCurrency)", formatMajor(estimatedMinor))
        }

        val dir = File(context.cacheDir, "reports").apply { mkdirs() }
        val file = File(dir, "freelanceflow-report-${System.currentTimeMillis()}.csv")
        file.writeText(csv.toString())
        return file
    }

    fun shareUriFor(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    private fun convertedMinor(amounts: List<Money>, rates: Map<String, Double>): Long =
        amounts.sumOf { amount ->
            val rate = rates[amount.currencyCode] ?: return@sumOf 0L
            (amount.minorUnits * rate).roundToLong()
        }

    private fun convertedDeductibleMinor(expenses: List<Expense>, rates: Map<String, Double>): Long =
        expenses.sumOf { expense ->
            val rate = rates[expense.amount.currencyCode] ?: return@sumOf 0L
            (expense.amount.minorUnits * (expense.deductiblePercent / 100.0) * rate).roundToLong()
        }

    // Locale.US, not getDefault(): CSV fields must use "." for decimals since "," is the delimiter.
    private fun formatMajor(minorUnits: Long): String = String.format(Locale.US, "%.2f", minorUnits / 100.0)

    private fun escapeCsv(value: String): String =
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
}
