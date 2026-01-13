package com.example.myapplication.database

import androidx.room.*@androidx.room.Dao
interface InfraccionDao {
    @androidx.room.Insert
    suspend fun insertar(infraccion: InfraccionPendiente)

    @androidx.room.Query("SELECT * FROM infracciones_pendientes")
    suspend fun obtenerTodas(): List<InfraccionPendiente>

    @androidx.room.Delete
    suspend fun eliminar(infraccion: InfraccionPendiente)
}