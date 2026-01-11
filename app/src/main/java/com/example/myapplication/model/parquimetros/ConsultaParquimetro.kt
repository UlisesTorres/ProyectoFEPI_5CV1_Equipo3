package com.example.myapplication.model.parquimetros

data class ConsultaParquimetro(
    val fecha_inicio: String,
    val zona: String,
    val vigente: Boolean,
    val placa_vehiculo: String
)