package com.travelbudget.app.presentation.addedit

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showDeleteDialog by remember { mutableStateOf(false) }

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
        onDeleteClick = {
            showDeleteDialog = true
        }
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Expense") },
            text = { Text("Are you sure you want to delete this expense?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteExpense(expenseId)
                        showDeleteDialog = false
                        onSaveClick()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
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