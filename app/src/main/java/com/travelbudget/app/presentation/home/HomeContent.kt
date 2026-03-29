package com.travelbudget.app.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.travelbudget.app.data.model.Expense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onAddClick: () -> Unit,
    onShareClick: () -> Unit,
    onExpenseClick: (String) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Travel Budget") },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                TotalSpendingCard(uiState.totalAmount)
            }

            item {
                CategorySummarySection(uiState.categoryTotals)
            }

            item {
                Text(
                    text = "Recent Expenses",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (uiState.expenses.isEmpty()) {
                item {
                    Text(
                        text = "No expenses added yet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(uiState.expenses) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onClick = { expenseId ->
                            onExpenseClick(expenseId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TotalSpendingCard(total: Double) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Total Spending",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹ %.2f".format(total),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun CategorySummarySection(categoryTotals: Map<String, Double>) {

    val categories = listOf("Food", "Hotel", "Transport", "Other")

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Category Summary",
                style = MaterialTheme.typography.titleMedium
            )

            categories.forEach { category ->
                val amount = categoryTotals[category] ?: 0.0
                Text("$category: ₹ %.2f".format(amount))
            }
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onClick: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(expense.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(expense.title, style = MaterialTheme.typography.titleMedium)
                Text(expense.category, style = MaterialTheme.typography.bodySmall)
                Text(expense.date, style = MaterialTheme.typography.bodySmall)
            }

            Text(
                text = "₹ %.2f".format(expense.amount),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}