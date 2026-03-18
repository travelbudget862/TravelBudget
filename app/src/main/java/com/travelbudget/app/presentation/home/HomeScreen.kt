package com.travelbudget.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.ui.theme.TravelBudgetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    expenses: List<Expense> = emptyList(),
    totalAmount: Double = 0.0,
    categoryTotals: Map<String, Double> = emptyMap(),
    onAddClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
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
                TotalSpendingCard(totalAmount)
            }

            item {
                CategorySummarySection(categoryTotals)
            }

            item {
                Text(
                    text = "Recent Expenses",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (expenses.isEmpty()) {
                item {
                    Text(
                        text = "No expenses added yet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(expenses) { expense ->
                    ExpenseItem(expense)
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
fun ExpenseItem(expense: Expense) {

    Card(
        modifier = Modifier.fillMaxWidth()
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

    val categoryTotals = mapOf(
        "Food" to 250.0,
        "Hotel" to 3000.0,
        "Transport" to 450.0,
        "Other" to 0.0
    )

    TravelBudgetTheme {
        HomeScreen(
            expenses = sampleExpenses,
            totalAmount = 3700.0,
            categoryTotals = categoryTotals,
            onAddClick = {},
            onShareClick = {}
        )
    }
}