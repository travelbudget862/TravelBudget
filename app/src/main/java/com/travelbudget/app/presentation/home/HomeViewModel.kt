package com.travelbudget.app.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.travelbudget.app.core.network.NetworkMonitor
import com.travelbudget.app.data.local.TravelBudgetDatabase
import com.travelbudget.app.data.local.toEntity
import com.travelbudget.app.data.local.toExpense
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FirestoreRepository()
    private val database = TravelBudgetDatabase.getDatabase(application)
    private val dao = database.expenseDao()
    private val networkMonitor = NetworkMonitor(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        observeNetwork()
        loadCachedExpenses()
        startListening()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                _uiState.value = _uiState.value.copy(
                    isOffline = !isOnline
                )
            }
        }
    }

    private fun loadCachedExpenses() {
        viewModelScope.launch {
            val cachedExpenses = dao.getAllExpenses()
                .map { it.toExpense() }

            if (cachedExpenses.isNotEmpty()) {
                updateUiState(cachedExpenses)
            }
        }
    }

    private fun startListening() {

        if (listenerRegistration != null) return

        listenerRegistration = repository.getExpensesQuery()
            .addSnapshotListener { snapshot, error ->

                if (error != null) return@addSnapshotListener

                val expenses =
                    snapshot?.toObjects(Expense::class.java)
                        ?: emptyList()

                updateUiState(expenses)

                viewModelScope.launch {
                    dao.insertExpenses(
                        expenses.map { it.toEntity() }
                    )
                }
            }
    }

    fun generateShareSummary(): String {

        val state = _uiState.value

        val builder = StringBuilder()

        builder.appendLine("TravelBudget Summary")
        builder.appendLine("---------------------------")
        builder.appendLine("Total Spending: ₹ %.2f".format(state.totalAmount))
        builder.appendLine()

        builder.appendLine("Category Breakdown:")
        state.categoryTotals.forEach { (category, amount) ->
            builder.appendLine("$category: ₹ %.2f".format(amount))
        }

        builder.appendLine()
        builder.appendLine("Generated via TravelBudget App")

        return builder.toString()
    }

    private fun updateUiState(expenses: List<Expense>) {

        val totalAmount = expenses.sumOf { it.amount }

        val categoryTotals = mapOf(
            "Food" to expenses.filter { it.category == "Food" }.sumOf { it.amount },
            "Hotel" to expenses.filter { it.category == "Hotel" }.sumOf { it.amount },
            "Transport" to expenses.filter { it.category == "Transport" }.sumOf { it.amount },
            "Other" to expenses.filter { it.category == "Other" }.sumOf { it.amount }
        )

        _uiState.value = _uiState.value.copy(
            expenses = expenses,
            totalAmount = totalAmount,
            categoryTotals = categoryTotals,
            isLoading = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}