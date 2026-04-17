package com.travelbudget.app.presentation.home

import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.ui.theme.TravelBudgetTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onAddClick: () -> Unit = {},
    onExpenseClick: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState()

    var showSharePreview by remember { mutableStateOf(false) }

    val shareText = remember(state.value) {
        viewModel.generateShareSummary()
    }

    HomeContent(
        uiState = state.value,
        onAddClick = onAddClick,
        onShareClick = { showSharePreview = true },
        onExpenseClick = onExpenseClick,
        onSettingsClick = onSettingsClick
    )

    if (showSharePreview) {
        AlertDialog(
            onDismissRequest = { showSharePreview = false },
            title = { Text("Share Summary") },
            text = {
                Text(shareText)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSharePreview = false

                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }

                        context.startActivity(
                            Intent.createChooser(intent, "Share via")
                        )
                    }
                ) {
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSharePreview = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
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