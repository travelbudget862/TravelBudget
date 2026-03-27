package com.travelbudget.app.presentation.addedit

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddEditExpenseViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _uiState = MutableStateFlow(AddEditExpenseUiState())
    val uiState: StateFlow<AddEditExpenseUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun saveExpense() {

        val currentState = _uiState.value

        if (currentState.title.isBlank() ||
            currentState.amount.isBlank() ||
            currentState.date.isBlank()
        ) return

        val expense = Expense(
            title = currentState.title,
            category = currentState.category,
            amount = currentState.amount.toDoubleOrNull() ?: 0.0,
            date = currentState.date,
            timestamp = Timestamp.now()
        )

        repository.addExpense(expense)

        _uiState.value = AddEditExpenseUiState()
    }
}