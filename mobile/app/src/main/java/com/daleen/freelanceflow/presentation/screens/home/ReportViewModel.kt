package com.daleen.freelanceflow.presentation.screens.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import com.daleen.freelanceflow.domain.repository.ClientRepository
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import com.daleen.freelanceflow.domain.repository.InvoiceRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import com.daleen.freelanceflow.domain.repository.TaxProfileRepository
import com.daleen.freelanceflow.report.ReportGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class ReportPeriod(val label: String) {
    THIS_MONTH("This month"),
    THIS_YEAR("This year"),
    ALL_TIME("All time"),
}

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val expenseRepository: ExpenseRepository,
    private val clientRepository: ClientRepository,
    private val projectRepository: ProjectRepository,
    private val taxProfileRepository: TaxProfileRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val businessProfileRepository: BusinessProfileRepository,
    private val reportGenerator: ReportGenerator,
) : ViewModel() {

    private val _period = MutableStateFlow(ReportPeriod.ALL_TIME)
    val period: StateFlow<ReportPeriod> = _period

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting

    private val _shareEvent = MutableSharedFlow<Uri>(extraBufferCapacity = 1)
    val shareEvent: SharedFlow<Uri> = _shareEvent

    fun setPeriod(period: ReportPeriod) {
        _period.value = period
    }

    fun exportCsv() {
        if (_isExporting.value) return
        viewModelScope.launch {
            _isExporting.value = true
            try {
                val period = _period.value
                val start = periodStart(period)

                val invoices = invoiceRepository.observeAll().first()
                    .filter { start == null || it.issueDate >= start }
                val expenses = expenseRepository.observeAll().first()
                    .filter { start == null || it.date >= start }
                val clients = clientRepository.observeAll().first()
                val projects = projectRepository.observeAll().first()
                val base = businessProfileRepository.baseCurrency.first()
                val deductionRatePercent = taxProfileRepository.observeProfile().first()?.deductionRatePercent

                val currencies = (invoices.map { it.billingCurrency } +
                    expenses.map { it.amount.currencyCode }).toSet() + base
                val rates = ratesTo(base, currencies)

                val file = reportGenerator.generate(
                    period = period,
                    baseCurrency = base,
                    invoices = invoices,
                    expenses = expenses,
                    clients = clients,
                    projects = projects,
                    rates = rates,
                    deductionRatePercent = deductionRatePercent,
                )
                _shareEvent.emit(reportGenerator.shareUriFor(file))
            } finally {
                _isExporting.value = false
            }
        }
    }

    private suspend fun ratesTo(base: String, currencies: Set<String>): Map<String, Double> {
        val rates = mutableMapOf<String, Double>()
        for (currency in currencies) {
            if (currency == base) {
                rates[currency] = 1.0
                continue
            }
            val outcome = exchangeRateRepository.getRate(currency, base)
            if (outcome is ExchangeRateOutcome.Available) rates[currency] = outcome.rate
        }
        return rates
    }

    private fun periodStart(period: ReportPeriod): Long? {
        if (period == ReportPeriod.ALL_TIME) return null
        val calendar = Calendar.getInstance()
        when (period) {
            ReportPeriod.THIS_MONTH -> calendar.set(Calendar.DAY_OF_MONTH, 1)
            ReportPeriod.THIS_YEAR -> calendar.set(Calendar.DAY_OF_YEAR, 1)
            ReportPeriod.ALL_TIME -> Unit
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
