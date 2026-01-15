package com.example.myapplication.model.operador_grua

import com.google.gson.annotations.SerializedName

// Modelo para la petición de actualización
data class UpdateArrastreRequest(
    @SerializedName("data")
    val data: UpdateArrastreData
)

data class UpdateArrastreData(
    @SerializedName("estatus")
    val estatus: Int
)

// Modelo para la respuesta
data class UpdateArrastreResponse(
    @SerializedName("data")
    val data: GenerarArrastreAttributes
)