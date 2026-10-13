package com.daleen.freelanceflow.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class FreelanceFlowDestination(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Filled.Home),
    WORK("work", "Work", Icons.Filled.Work),
    MONEY("money", "Money", Icons.Filled.AccountBalanceWallet),
    CLIENTS("clients", "Clients", Icons.Filled.People),
}
