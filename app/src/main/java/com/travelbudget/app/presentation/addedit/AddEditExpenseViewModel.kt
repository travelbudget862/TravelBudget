package com.travelbudget.app.presentation.addedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.travelbudget.app.data.local.TravelBudgetDatabase
import com.travelbudget.app.data.local.toEntity
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddEditExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FirestoreRepository()

    private val database = TravelBudgetDatabase.getDatabase(application)
    private val dao = database.expenseDao()

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

        if (
            current.title.isBlank() ||
            current.amount.isBlank() ||
            current.date.isBlank()
        ) return

        val expense = Expense(
            id = expenseId ?: UUID.randomUUID().toString(), // important
            title = current.title,
            category = current.category,
            amount = current.amount.toDoubleOrNull() ?: 0.0,
            date = current.date,
            timestamp = Timestamp.now()
        )

        viewModelScope.launch {
            dao.insertExpenses(listOf(expense.toEntity()))
        }

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