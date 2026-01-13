package com.example.myapplication.database

import androidx.room.*

@Dao
interface InfraccionDao {
    @Insert
    suspend fun insertar(infraccion: InfraccionPendiente)

    @Query("SELECT * FROM infracciones_pendientes")
    suspend fun obtenerTodas(): List<InfraccionPendiente>

    @Delete
    suspend fun eliminar(infraccion: InfraccionPendiente)
}
