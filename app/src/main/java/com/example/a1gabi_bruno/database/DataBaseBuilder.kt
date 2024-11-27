package com.example.a1gabi_bruno.database

import android.content.Context
import androidx.room.Room


object DatabaseBuilder {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "knkdatabase"
            )
                .fallbackToDestructiveMigration() // Garante que o banco será recriado se houver mudanças
                .build()
            INSTANCE = instance
            instance
        }
    }
}
