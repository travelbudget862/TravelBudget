package com.travelbudget.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ExpenseEntity::class],
    version = 1
)
abstract class TravelBudgetDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {

        @Volatile
        private var INSTANCE: TravelBudgetDatabase? = null

        fun getDatabase(context: Context): TravelBudgetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelBudgetDatabase::class.java,
                    "travel_budget_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}