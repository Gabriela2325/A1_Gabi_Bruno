package com.example.a1gabi_bruno.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a1gabi_bruno.dao.CadernoDao
import com.example.a1gabi_bruno.dao.LivroDao
import com.example.a1gabi_bruno.model.Caderno
import com.example.a1gabi_bruno.model.Livro


// Liste a entidade 'Cadernos' no parâmetro 'entities'
@Database(entities = [Caderno::class, Livro::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cadernosDao(): CadernoDao
    abstract fun livroDao(): LivroDao
}
