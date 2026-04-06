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

    private val database =
        TravelBudgetDatabase.getDatabase(application)

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

        listenerRegistration = repository.getExpensesQuery()
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    return@addSnapshotListener
                }

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

    private fun updateUiState(expenses: List<Expense>) {

        val totalAmount = expenses.sumOf { it.amount }

        val foodTotal = expenses
            .filter { it.category == "Food" }
            .sumOf { it.amount }

        val hotelTotal = expenses
            .filter { it.category == "Hotel" }
            .sumOf { it.amount }

        val transportTotal = expenses
            .filter { it.category == "Transport" }
            .sumOf { it.amount }

        val otherTotal = expenses
            .filter { it.category == "Other" }
            .sumOf { it.amount }

        val categoryTotals = mapOf(
            "Food" to foodTotal,
            "Hotel" to hotelTotal,
            "Transport" to transportTotal,
            "Other" to otherTotal
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