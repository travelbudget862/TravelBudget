package com.travelbudget.app.presentation.addedit

data class AddEditExpenseUiState(
    val title: String = "",
    val category: String = "Food",
    val amount: String = "",
    val date: String = "",
    val isEditMode: Boolean = false
)