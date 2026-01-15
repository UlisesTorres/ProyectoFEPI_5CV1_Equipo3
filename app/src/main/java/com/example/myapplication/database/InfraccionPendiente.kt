package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infracciones_pendientes")
data class InfraccionPendiente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val folio: String,

    @ColumnInfo(name = "tipo_infraccion_id")
    val tipoInfraccionId: Int,

    @ColumnInfo(name = "articulo_id")
    val articuloId: Int,

    val placa: String,
    val ubicacion: String,
    val fecha: String,
    val rutaFirma: String,
    val rutasFotos: String
)
