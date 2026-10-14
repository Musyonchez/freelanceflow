package com.daleen.freelanceflow.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daleen.freelanceflow.domain.model.AuthState
import com.daleen.freelanceflow.presentation.navigation.FreelanceFlowDestination
import com.daleen.freelanceflow.presentation.navigation.FreelanceFlowNavHost
import com.daleen.freelanceflow.presentation.screens.auth.AuthScreen
import com.daleen.freelanceflow.presentation.screens.auth.AuthViewModel

@Composable
fun FreelanceFlowApp(authViewModel: AuthViewModel = hiltViewModel()) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    when (val state = authState) {
        is AuthState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is AuthState.SignedOut -> AuthScreen(viewModel = authViewModel)
        is AuthState.SignedIn -> SignedInApp(onSignOut = authViewModel::signOut)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignedInApp(onSignOut: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = FreelanceFlowDestination.entries.find { destination ->
                backStackEntry?.destination?.hierarchy?.any { it.route == destination.route } == true
            }

            TopAppBar(
                title = { Text(currentDestination?.label ?: "FreelanceFlow") },
                actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign out")
                    }
                },
            )
        },
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination

            NavigationBar {
                FreelanceFlowDestination.entries.forEach { destination ->
                    val selected = currentRoute?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        FreelanceFlowNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
