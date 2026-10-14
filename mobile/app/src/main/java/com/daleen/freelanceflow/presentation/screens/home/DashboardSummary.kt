package com.daleen.freelanceflow.presentation.screens.home

import com.daleen.freelanceflow.domain.model.Money

data class DashboardSummary(
    val baseCurrency: String,
    val outstandingTotal: Money,
    val paidTotal: Money,
    val expenseTotal: Money,
    /** Outstanding invoice totals grouped by their own billing currency, unconverted. */
    val outstandingByCurrency: List<Pair<String, Money>>,
    val isLoading: Boolean,
) {
    companion object {
        fun loading(baseCurrency: String) = DashboardSummary(
            baseCurrency = baseCurrency,
            outstandingTotal = Money.zero(baseCurrency),
            paidTotal = Money.zero(baseCurrency),
            expenseTotal = Money.zero(baseCurrency),
            outstandingByCurrency = emptyList(),
            isLoading = true,
        )
    }
}
