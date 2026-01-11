package com.example.myapplication.model.pago

data class Pago(
    val linea_captura: String,
    val monto: Double,
    val vigencia: String,
    val fecha_pago: String,
    val estatus: String,
    val infraccion_id: Int
)