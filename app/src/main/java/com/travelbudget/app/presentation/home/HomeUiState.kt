package com.travelbudget.app.presentation.home

import com.travelbudget.app.data.model.Expense

data class HomeUiState(
    val expenses: List<Expense> = emptyList(),
    val totalAmount: Double = 0.0,
    val categoryTotals: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = true
)