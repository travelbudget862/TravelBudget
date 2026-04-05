package com.travelbudget.app.data.local

import com.google.firebase.Timestamp
import com.travelbudget.app.data.model.Expense

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        category = category,
        amount = amount,
        date = date,
        timestamp = timestamp.seconds
    )
}

fun ExpenseEntity.toExpense(): Expense {
    return Expense(
        id = id,
        title = title,
        category = category,
        amount = amount,
        date = date,
        timestamp = Timestamp(timestamp, 0)
    )
}