package com.example.myapplication.model.operador_grua

import com.google.gson.annotations.SerializedName

// --- Modelo para enviar la petición POST ---
data class OrdenArrastreRequest(
    @SerializedName("data")
    val data: OrdenArrastreData
)

data class OrdenArrastreData(
    @SerializedName("fecha_ingreso")
    val fechaIngreso: String,

    @SerializedName("estatus")
    val estatus: String,

    @SerializedName("operador_grua")
    val operadorGrua: String,

    @SerializedName("grua_identificador")
    val gruaIdentificador: String,

    // 👇 RELACIÓN STRAPI V5 (documentId)

    @SerializedName("infraccion_id")
    val infraccionId: Int,
)


// --- Modelo para la respuesta (si es necesario) ---
data class OrdenArrastreResponse(
    @SerializedName("data")
    val data: OrdenArrastreDetails
)

data class OrdenArrastreDetails(
    @SerializedName("id")
    val id: Int,

    @SerializedName("attributes")
    val attributes: OrdenArrastreData
)
