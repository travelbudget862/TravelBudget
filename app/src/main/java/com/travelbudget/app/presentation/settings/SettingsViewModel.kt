package com.travelbudget.app.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.travelbudget.app.data.local.TravelBudgetDatabase
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val database =
        TravelBudgetDatabase.getDatabase(application)

    private val dao = database.expenseDao()

    fun clearLocalCache(onComplete: () -> Unit) {
        viewModelScope.launch {
            dao.clearAll()
            onComplete()
        }
    }
}