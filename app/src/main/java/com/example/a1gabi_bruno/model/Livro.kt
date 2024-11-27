package com.example.a1gabi_bruno.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "livro")
data class Livro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val autor: String,
    val genero: String,
    val statusLeitura: String,
    val anotacoes: String
)
