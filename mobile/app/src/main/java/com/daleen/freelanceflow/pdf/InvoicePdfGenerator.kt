package com.daleen.freelanceflow.pdf

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import android.net.Uri
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Renders an invoice to a one-page PDF with Android's native PdfDocument/Canvas APIs --
 * no extra PDF library dependency, per docs/09-api-and-integrations.md.
 */
class InvoicePdfGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

    fun generate(invoice: Invoice, items: List<InvoiceItem>, client: Client, businessName: String): File {
        val pageWidth = 595
        val pageHeight = 842
        val left = 40f
        val right = 555f

        val document = PdfDocument()
        val page = document.startPage(
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create(),
        )
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true; textSize = 12f }
        var y = 50f

        fun line(text: String, size: Float = 12f, bold: Boolean = false, x: Float = left) {
            paint.textSize = size
            paint.isFakeBoldText = bold
            canvas.drawText(text, x, y, paint)
        }

        line(businessName.ifBlank { "Your Business Name" }, size = 16f, bold = true)
        y += 34f
        line("INVOICE", size = 22f, bold = true)
        y += 28f

        line("Invoice #: ${invoice.invoiceNumber}")
        y += 16f
        line("Issue date: ${dateFormat.format(Date(invoice.issueDate))}")
        y += 16f
        line("Due date: ${dateFormat.format(Date(invoice.dueDate))}")
        y += 16f
        line("Status: ${invoice.status.name}")
        y += 26f

        line("Bill to:", bold = true)
        y += 16f
        line(client.name)
        y += 16f
        client.company?.let { line(it); y += 16f }
        client.email?.let { line(it); y += 16f }
        y += 16f

        line("Description", bold = true)
        line("Qty", bold = true, x = 330f)
        line("Unit price", bold = true, x = 400f)
        line("Line total", bold = true, x = 490f)
        y += 6f
        canvas.drawLine(left, y, right, y, paint)
        y += 18f

        items.forEach { item ->
            line(item.description.take(42))
            line(item.quantity.toString(), x = 330f)
            line(item.unitPrice.format(), x = 400f)
            line(item.lineTotal.format(), x = 490f)
            y += 18f
        }
        canvas.drawLine(left, y, right, y, paint)
        y += 22f

        line("Subtotal: ${invoice.subtotal.format()}", x = 380f)
        y += 18f

        if (invoice.baseCurrency != invoice.billingCurrency) {
            line(
                "Rate used: 1 ${invoice.baseCurrency} = ${"%.4f".format(invoice.exchangeRate)} " +
                    "${invoice.billingCurrency} (as of ${dateTimeFormat.format(Date(invoice.exchangeRateTimestamp))})",
                size = 9f,
            )
            y += 16f
        }

        line("Total: ${invoice.total.format()}", size = 16f, bold = true, x = 380f)
        y += 32f

        invoice.notes?.takeIf { it.isNotBlank() }?.let {
            line("Notes: $it", size = 10f)
        }

        document.finishPage(page)

        val dir = File(context.cacheDir, "invoices").apply { mkdirs() }
        val file = File(dir, "${invoice.invoiceNumber}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }

    fun shareUriFor(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
