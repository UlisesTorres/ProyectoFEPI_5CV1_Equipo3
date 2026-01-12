package com.example.myapplication.model.parquimetros

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta completa de la API.
 * Contiene directamente una lista de los atributos del parquímetro.
 */data class ParquimetroResponse(
    @SerializedName("data")
    val data: List<ParquimetroAttributes>
)

/**
 * Contiene los campos específicos de un registro de parquímetro.
 * Esta clase AHORA representa cada objeto dentro del array "data".
 */
data class ParquimetroAttributes(
    // --- Asegúrate de que todos estos campos estén presentes ---

    @SerializedName("fecha_inicio")
    val fechaInicio: String,

    @SerializedName("zona")
    val zona: String,

    // --- El compilador no encuentra este campo ---
    @SerializedName("vigente")
    val vigente: Boolean,

    // --- Y tampoco encuentra este campo ---
    @SerializedName("placa_vehiculo")
    val placaVehiculo: String
)
