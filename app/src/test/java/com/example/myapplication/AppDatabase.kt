package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [InfraccionPendiente::class], version = 10)
abstract class AppDatabase : RoomDatabase() {
    abstract fun infraccionDao(): InfraccionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "infracciones_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}