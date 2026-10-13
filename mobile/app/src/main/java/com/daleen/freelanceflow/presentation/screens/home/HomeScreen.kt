package com.daleen.freelanceflow.presentation.screens.home

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = "Good evening",
        subtitle = "Your dashboard (balance, timer, projects, invoices) lands here next.",
        modifier = modifier,
    )
}
