package com.example.myapplication.model.transito

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

    @SerializedName("placa")
    val placa: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("hora_inicio")
    val horaInicio: String,

    @SerializedName("hora_termino")
    val horaTermino: String
)
