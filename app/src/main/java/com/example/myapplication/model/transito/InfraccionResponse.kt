package com.example.myapplication.model.transito

import com.google.gson.annotations.SerializedName

/**
 * Representa el objeto de nivel superior de la respuesta de la API,
 * que contiene una lista de infracciones.
 */
data class InfraccionesResponse(
    @SerializedName("data")
    val data: List<InfraccionAttributes> // <-- CORRECCIÓN #1: Ahora contiene directamente una lista de InfraccionAttributes.
)

/**
 * Representa los campos de una sola infracción.
 * La clase InfraccionData ha sido eliminada porque era innecesaria.
 */
data class InfraccionAttributes(
    @SerializedName("folio")
    val folio: String,

    @SerializedName("placa_vehiculo")
    val placa: String, // El nombre coincide con el JSON: "placa_vehiculo"

    @SerializedName("fecha_infraccion")
    val fecha: String  // El nombre coincide con el JSON: "fecha_infraccion"
)

// La clase `InfraccionData` que tenías antes debe ser eliminada.
// No la necesitas porque el JSON del servidor no tiene el nivel "attributes".

