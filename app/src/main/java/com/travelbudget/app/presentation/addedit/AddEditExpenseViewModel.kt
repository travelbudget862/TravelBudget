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

    fun saveExpense(expenseId: String? = null) {

        val current = _uiState.value

        if (current.title.isBlank() ||
            current.amount.isBlank() ||
            current.date.isBlank()
        ) return

        val expense = Expense(
            id = expenseId ?: "",
            title = current.title,
            category = current.category,
            amount = current.amount.toDoubleOrNull() ?: 0.0,
            date = current.date,
            timestamp = com.google.firebase.Timestamp.now()
        )

        if (expenseId == null) {
            repository.addExpense(expense)
        } else {
            repository.updateExpense(expense)
        }
    }

    fun loadExpense(expenseId: String) {

        repository.getExpenseById(expenseId)
            .get()
            .addOnSuccessListener { document ->

                val expense = document.toObject(Expense::class.java)

                expense?.let {
                    _uiState.value = AddEditExpenseUiState(
                        title = it.title,
                        category = it.category,
                        amount = it.amount.toString(),
                        date = it.date,
                        isEditMode = true
                    )
                }
            }
    }

    fun deleteExpense(expenseId: String?) {
        if (expenseId == null) return
        repository.deleteExpense(expenseId)
    }
}