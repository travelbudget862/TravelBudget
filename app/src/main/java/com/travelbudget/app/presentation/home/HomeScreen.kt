package com.travelbudget.app.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.ui.theme.TravelBudgetTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onAddClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onExpenseClick: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    val state = viewModel.uiState.collectAsState()

    HomeContent(
        uiState = state.value,
        onAddClick = onAddClick,
        onShareClick = onShareClick,
        onExpenseClick = onExpenseClick,
        onSettingsClick = onSettingsClick
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val sampleExpenses = listOf(
        Expense(
            id = "1",
            title = "Lunch",
            category = "Food",
            amount = 250.0,
            date = "24 Feb 2026"
        ),
        Expense(
            id = "2",
            title = "Hotel Stay",
            category = "Hotel",
            amount = 3000.0,
            date = "23 Feb 2026"
        ),
        Expense(
            id = "3",
            title = "Taxi",
            category = "Transport",
            amount = 450.0,
            date = "22 Feb 2026"
        )
    )

    val previewState = HomeUiState(
        expenses = sampleExpenses,
        totalAmount = 3700.0,
        categoryTotals = mapOf(
            "Food" to 250.0,
            "Hotel" to 3000.0,
            "Transport" to 450.0,
            "Other" to 0.0
        ),
        isLoading = false
    )

    TravelBudgetTheme {
        HomeContent(
            uiState = previewState,
            onAddClick = {},
            onShareClick = {},
            onExpenseClick = {},
            onSettingsClick = {}
        )
    }
}