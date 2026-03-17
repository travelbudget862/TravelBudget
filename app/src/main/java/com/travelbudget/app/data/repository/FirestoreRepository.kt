package com.travelbudget.app.data.repository

import com.travelbudget.app.data.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private fun getUserExpensesRef() =
        firestore.collection("users")
            .document(auth.currentUser?.uid ?: "")
            .collection("expenses")

    fun addExpense(expense: Expense) {
        val docRef = getUserExpensesRef().document()
        docRef.set(expense.copy(id = docRef.id))
    }

    fun updateExpense(expense: Expense) {
        getUserExpensesRef()
            .document(expense.id)
            .set(expense)
    }

    fun deleteExpense(expenseId: String) {
        getUserExpensesRef()
            .document(expenseId)
            .delete()
    }

    fun getExpensesQuery(): Query {
        return getUserExpensesRef()
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }
}