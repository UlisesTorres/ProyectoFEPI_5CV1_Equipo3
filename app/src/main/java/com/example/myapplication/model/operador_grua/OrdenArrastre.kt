package com.example.myapplication.model.operador_grua

data class OrdenArrastre(
    val fecha_ingreso: String,
    val fecha_liberacion: String,
    val estatus: String,
    val operador_grua: String,
    val grua_identificador: String,
    val infraccion_id: Int
)