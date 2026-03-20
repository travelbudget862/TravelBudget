package com.travelbudget.app.presentation.home

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.travelbudget.app.data.model.Expense
import com.travelbudget.app.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        startListening()
    }

    private fun startListening() {

        listenerRegistration = repository.getExpensesQuery()
            .addSnapshotListener { snapshot, _ ->

                val expenses = snapshot?.toObjects(Expense::class.java) ?: emptyList()

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

                _uiState.value = HomeUiState(
                    expenses = expenses,
                    totalAmount = totalAmount,
                    categoryTotals = categoryTotals,
                    isLoading = false
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}