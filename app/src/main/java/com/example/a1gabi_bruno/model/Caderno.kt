package com.example.a1gabi_bruno.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Caderno")
data class Caderno(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descricao: String,
    val dataCriacao: Long
)