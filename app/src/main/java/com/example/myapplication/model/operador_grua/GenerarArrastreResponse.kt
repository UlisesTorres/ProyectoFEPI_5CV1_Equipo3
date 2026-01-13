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

    @SerializedName("infraccion_id")
    val infraccion_id: PopulatedInfraccion?
)

// --- CORRECCIÓN: El modelo ahora puede recibir la ubicación y el oficial ---
data class PopulatedInfraccion(
    @SerializedName("id")
    val id: Int,

    @SerializedName("folio")
    val folio: String?,

    @SerializedName("ubicacion_infraccion")
    val ubicacion: String?,

    @SerializedName("oficial_id")
    val oficial: PopulatedOficial?
)

data class PopulatedOficial(
    @SerializedName("id")
    val id: Int
)
