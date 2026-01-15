package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infracciones_pendientes")
data class InfraccionPendiente(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val folio: String,
    val tipoInfraccionId: List<Int>,
    val articuloId: List<Int>,
    val placa: String,
    val ubicacion: String,
    val fecha: String,
    val rutaFirma: String,
    val rutasFotos: String
)
