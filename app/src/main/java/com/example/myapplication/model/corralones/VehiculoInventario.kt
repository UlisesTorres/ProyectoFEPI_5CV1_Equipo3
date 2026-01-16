package com.example.myapplication.model.corralones

data class VehiculoInventario(
    val id: String,
    val documentId: String,
    val folio: String,
    val placa: String,
    val estatus: String,
    val fechaIngreso: String,
    val ubicacion: String,
    val observaciones: String,
    val pagoEstatus: String // pendiente, pagado, vencido
)
