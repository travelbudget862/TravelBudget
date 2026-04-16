package com.travelbudget.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Home

@Serializable
object Auth

@Serializable
data class AddEditExpense(
    val expenseId: String? = null
)

@Serializable
object Settings