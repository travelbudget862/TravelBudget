package com.travelbudget.app.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.travelbudget.app.ui.theme.TravelBudgetTheme

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val destination = viewModel.destination.collectAsState().value

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Home -> onNavigateToHome()
            SplashDestination.Auth -> onNavigateToAuth()
            null -> Unit
        }
    }

    SplashContent()
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    TravelBudgetTheme {
        SplashContent()
    }
}