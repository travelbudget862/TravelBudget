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

                val total = expenses.sumOf { it.amount }

                val categoryMap = expenses
                    .groupBy { it.category }
                    .mapValues { entry ->
                        entry.value.sumOf { it.amount }
                    }

                _uiState.value = HomeUiState(
                    expenses = expenses,
                    totalAmount = total,
                    categoryTotals = categoryMap,
                    isLoading = false
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}