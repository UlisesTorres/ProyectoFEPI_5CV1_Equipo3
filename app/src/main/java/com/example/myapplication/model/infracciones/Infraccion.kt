package com.example.myapplication.model.infracciones

data class Infraccion(
    val folio: String,
    val placa_vehiculo: String,
    val fecha_infraccion: String,
    val ubicacion: String,

    //Relaciones
    val tipo_infraccion_id: Int?,
    val articulo_id: Int?,

    //Multimedia
    val evidencia_infraccion:List<String>?,
    val firma_infractor: String?
)