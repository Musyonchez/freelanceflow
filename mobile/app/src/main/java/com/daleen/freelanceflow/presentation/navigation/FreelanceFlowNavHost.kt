package com.daleen.freelanceflow.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.daleen.freelanceflow.presentation.screens.clients.ClientsScreen
import com.daleen.freelanceflow.presentation.screens.home.HomeScreen
import com.daleen.freelanceflow.presentation.screens.money.MoneyScreen
import com.daleen.freelanceflow.presentation.screens.work.WorkScreen

@Composable
fun FreelanceFlowNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = FreelanceFlowDestination.HOME.route,
        modifier = modifier,
    ) {
        composable(FreelanceFlowDestination.HOME.route) { HomeScreen() }
        composable(FreelanceFlowDestination.WORK.route) { WorkScreen() }
        composable(FreelanceFlowDestination.MONEY.route) { MoneyScreen() }
        composable(FreelanceFlowDestination.CLIENTS.route) { ClientsScreen() }
    }
}
