package com.travelbudget.app.presentation.addedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddEditExpenseScreen(
    expenseId: String? = null,
    viewModel: AddEditExpenseViewModel = viewModel(),
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {

    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(expenseId) {
        if (expenseId != null) {
            viewModel.loadExpense(expenseId)
        }
    }

    AddEditExpenseContent(
        uiState = state.value,
        onTitleChange = viewModel::updateTitle,
        onCategoryChange = viewModel::updateCategory,
        onAmountChange = viewModel::updateAmount,
        onDateChange = viewModel::updateDate,
        onSaveClick = {
            viewModel.saveExpense(expenseId)
            onSaveClick()
        },
        onDeleteClick = onDeleteClick
    )
}

@Preview(showBackground = true)
@Composable
fun AddExpensePreview() {

    val previewState = AddEditExpenseUiState(
        title = "Dinner",
        category = "Food",
        amount = "500",
        date = "24 Feb 2026",
        isEditMode = false
    )

    AddEditExpenseContent(
        uiState = previewState,
        onTitleChange = {},
        onCategoryChange = {},
        onAmountChange = {},
        onDateChange = {},
        onSaveClick = {},
        onDeleteClick = {}
    )
}