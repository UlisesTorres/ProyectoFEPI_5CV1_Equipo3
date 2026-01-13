package com.example.myapplication.model.operador_grua

import com.google.gson.annotations.SerializedName

data class GenerarArrastreResponse(
    @SerializedName("data")
    val data: List<GenerarArrastreAttributes>
)

data class GenerarArrastreAttributes(
    @SerializedName("id")
    val id: Int,

    @SerializedName("folio")
    val folio: String,

    @SerializedName("observaciones")
    val observaciones: String,

    // --- CORRECCIÓN: El nombre del campo ahora coincide con el JSON ---
    @SerializedName("infraccion_id")
    val infraccion_id: PopulatedInfraccion?
)

data class PopulatedInfraccion(
    @SerializedName("id")
    val id: Int,

    @SerializedName("folio")
    val folio: String?
)
