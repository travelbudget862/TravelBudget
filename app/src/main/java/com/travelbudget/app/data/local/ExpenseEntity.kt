package com.travelbudget.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(

    @PrimaryKey
    val id: String,

    val title: String,

    val category: String,

    val amount: Double,

    val date: String,

    val timestamp: Long
)