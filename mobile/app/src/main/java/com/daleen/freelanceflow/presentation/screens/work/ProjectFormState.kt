package com.daleen.freelanceflow.presentation.screens.work

import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.model.RateType

data class ProjectFormState(
    val id: String? = null,
    val createdAt: Long? = null,
    val clientId: String? = null,
    val name: String = "",
    val description: String = "",
    val rateType: RateType = RateType.HOURLY,
    // Raw text input (e.g. "40" or "40.50"); parsed to minor units on save.
    val rateAmount: String = "",
    val currency: String = "USD",
    val status: ProjectStatus = ProjectStatus.ACTIVE,
) {
    val isEditing: Boolean get() = id != null
    val isValid: Boolean
        get() = clientId != null &&
            name.isNotBlank() &&
            currency.isNotBlank() &&
            rateAmount.toDoubleOrNull() != null
}
