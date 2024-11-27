package com.example.a1gabi_bruno.dao

//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.a1gabi_bruno.model.Caderno
//
//
//@Dao
//interface CadernoDao {
//
//    // Inserir um caderno
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(caderno: Caderno)
//
//    // Atualizar um caderno
//    @Update
//    suspend fun update(caderno: Caderno)
//
//    // Deletar um caderno
//    @Delete
//    suspend fun delete(caderno: Caderno)
//
//    // Buscar todos os cadernos
//    @Query("SELECT * FROM Caderno")
//    suspend fun getAll(): List<Caderno>
//
//    // Buscar caderno por ID
//    @Query("SELECT * FROM Caderno WHERE id = :id")
//    suspend fun getById(id: Int): Caderno?
//}

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a1gabi_bruno.model.Caderno

@Dao
interface CadernoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(caderno: Caderno)

    @Query("SELECT * FROM Caderno WHERE id = :id")
    suspend fun getCadernoById(id: Int): Caderno?

    @Update
    suspend fun update(caderno: Caderno)

    @Query("SELECT * FROM Caderno")
    suspend fun getAll(): List<Caderno>

}
