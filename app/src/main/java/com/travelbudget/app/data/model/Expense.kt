package com.travelbudget.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Expense(

    @DocumentId
    val id: String = "",

    val title: String = "",

    val category: String = "",

    val amount: Double = 0.0,

    val date: String = "",

    val timestamp: Timestamp = Timestamp.now()
)