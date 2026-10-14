package com.daleen.freelanceflow.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import com.daleen.freelanceflow.domain.repository.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToLong

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val expenseRepository: ExpenseRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val businessProfileRepository: BusinessProfileRepository,
) : ViewModel() {

    val baseCurrency: StateFlow<String> = businessProfileRepository.baseCurrency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "USD")

    private val _summary = MutableStateFlow(DashboardSummary.loading("USD"))
    val summary: StateFlow<DashboardSummary> = _summary

    init {
        viewModelScope.launch {
            combine(
                invoiceRepository.observeAll(),
                expenseRepository.observeAll(),
                businessProfileRepository.baseCurrency,
            ) { invoices, expenses, base -> Triple(invoices, expenses, base) }
                .collectLatest { (invoices, expenses, base) ->
                    _summary.update { it.copy(isLoading = true) }

                    val outstanding = invoices.filter { it.status != InvoiceStatus.PAID }
                    val paid = invoices.filter { it.status == InvoiceStatus.PAID }

                    val currencies = (outstanding.map { it.billingCurrency } +
                        paid.map { it.billingCurrency } +
                        expenses.map { it.amount.currencyCode }).toSet() + base
                    val rates = ratesTo(base, currencies)

                    _summary.value = DashboardSummary(
                        baseCurrency = base,
                        outstandingTotal = convertSum(outstanding.map { it.total }, rates, base),
                        paidTotal = convertSum(paid.map { it.total }, rates, base),
                        expenseTotal = convertSum(expenses.map { it.amount }, rates, base),
                        outstandingByCurrency = outstanding
                            .groupBy { it.billingCurrency }
                            .map { (currency, group) ->
                                currency to group.fold(Money.zero(currency)) { acc, inv -> acc + inv.total }
                            },
                        isLoading = false,
                    )
                }
        }
    }

    fun setBaseCurrency(code: String) {
        viewModelScope.launch { businessProfileRepository.setBaseCurrency(code.trim().uppercase()) }
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

    private fun convertSum(amounts: List<Money>, rates: Map<String, Double>, base: String): Money {
        val totalMinor = amounts.sumOf { amount ->
            val rate = rates[amount.currencyCode] ?: return@sumOf 0L
            (amount.minorUnits * rate).roundToLong()
        }
        return Money(totalMinor, base)
    }
}
