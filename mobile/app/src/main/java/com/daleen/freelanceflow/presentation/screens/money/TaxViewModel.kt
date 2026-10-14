package com.daleen.freelanceflow.presentation.screens.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.TaxProfile
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import com.daleen.freelanceflow.domain.repository.TaxProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TaxPeriod(val label: String) {
    THIS_MONTH("This month"),
    THIS_YEAR("This year"),
    ALL_TIME("All time"),
}

@HiltViewModel
class TaxViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val taxProfileRepository: TaxProfileRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val businessProfileRepository: BusinessProfileRepository,
) : ViewModel() {

    val profile: StateFlow<TaxProfile?> = taxProfileRepository.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _period = MutableStateFlow(TaxPeriod.THIS_MONTH)
    val period: StateFlow<TaxPeriod> = _period

    private val _summary = MutableStateFlow(DeductionSummary.loading("USD"))
    val summary: StateFlow<DeductionSummary> = _summary

    private val _formState = MutableStateFlow<TaxProfileFormState?>(null)
    val formState: StateFlow<TaxProfileFormState?> = _formState

    init {
        viewModelScope.launch {
            combine(
                expenseRepository.observeAll(),
                taxProfileRepository.observeProfile(),
                businessProfileRepository.baseCurrency,
                _period,
            ) { expenses, profile, base, period -> Pack(expenses, profile, base, period) }
                .collectLatest { pack ->
                    _summary.update { it.copy(isLoading = true) }

                    val periodStart = periodStart(pack.period)
                    val deductible = pack.expenses.filter {
                        it.deductible && (periodStart == null || it.date >= periodStart)
                    }

                    val rates = ratesTo(pack.base, deductible.map { it.amount.currencyCode }.toSet() + pack.base)
                    val deductibleMinor = deductible.sumOf { expense ->
                        val rate = rates[expense.amount.currencyCode] ?: return@sumOf 0L
                        (expense.amount.minorUnits * (expense.deductiblePercent / 100.0) * rate).roundToLong()
                    }
                    val deductibleTotal = Money(deductibleMinor, pack.base)
                    val estimatedDeduction = Money(
                        (deductibleMinor * ((pack.profile?.deductionRatePercent ?: 0.0) / 100.0)).roundToLong(),
                        pack.base,
                    )

                    _summary.value = DeductionSummary(
                        baseCurrency = pack.base,
                        deductibleTotal = deductibleTotal,
                        estimatedDeduction = estimatedDeduction,
                        isLoading = false,
                    )
                }
        }
    }

    fun setPeriod(period: TaxPeriod) {
        _period.value = period
    }

    fun showProfileForm() {
        val current = profile.value
        _formState.value = TaxProfileFormState(
            label = current?.label ?: "",
            deductionRatePercent = current?.deductionRatePercent?.let { if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString() } ?: "",
            notes = current?.notes ?: "",
        )
    }

    fun hideProfileForm() {
        _formState.value = null
    }

    fun updateForm(transform: (TaxProfileFormState) -> TaxProfileFormState) {
        _formState.update { it?.let(transform) }
    }

    fun saveProfile() {
        val form = _formState.value ?: return
        val rate = form.deductionRatePercent.toDoubleOrNull() ?: return
        viewModelScope.launch {
            taxProfileRepository.upsert(
                TaxProfile(
                    id = profile.value?.id ?: UUID.randomUUID().toString(),
                    label = form.label.trim().ifEmpty { "My tax profile" },
                    deductionRatePercent = rate,
                    notes = form.notes.trim().ifEmpty { null },
                ),
            )
            _formState.value = null
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

    private fun periodStart(period: TaxPeriod): Long? {
        if (period == TaxPeriod.ALL_TIME) return null
        val calendar = Calendar.getInstance()
        when (period) {
            TaxPeriod.THIS_MONTH -> calendar.set(Calendar.DAY_OF_MONTH, 1)
            TaxPeriod.THIS_YEAR -> calendar.set(Calendar.DAY_OF_YEAR, 1)
            TaxPeriod.ALL_TIME -> Unit
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private data class Pack(
        val expenses: List<com.daleen.freelanceflow.domain.model.Expense>,
        val profile: TaxProfile?,
        val base: String,
        val period: TaxPeriod,
    )
}

data class TaxProfileFormState(
    val label: String = "",
    val deductionRatePercent: String = "",
    val notes: String = "",
)

data class DeductionSummary(
    val baseCurrency: String,
    val deductibleTotal: Money,
    val estimatedDeduction: Money,
    val isLoading: Boolean,
) {
    companion object {
        fun loading(baseCurrency: String) = DeductionSummary(
            baseCurrency = baseCurrency,
            deductibleTotal = Money.zero(baseCurrency),
            estimatedDeduction = Money.zero(baseCurrency),
            isLoading = true,
        )
    }
}
