package com.example.myapplication.model.operador_grua

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de la API al consultar la lista de solicitudes de arrastre.
 */
data class GenerarArrastreResponse(
    @SerializedName("data")    val data: List<GenerarArrastreAttributes> // <-- CORRECCIÓN #1: La lista ahora contiene 'GenerarArrastreAttributes' directamente.
)

// La clase GenerarArrastreData se elimina porque es innecesaria.

/**
 * Contiene los campos de una solicitud de arrastre.
 * Esta clase AHORA representa cada objeto dentro del array "data".
 */
data class GenerarArrastreAttributes(
    @SerializedName("id") // Es bueno tener el ID también
    val id: Int,

    @SerializedName("folio")
    val folio: String,

    @SerializedName("observaciones")
    val observaciones: String
)
