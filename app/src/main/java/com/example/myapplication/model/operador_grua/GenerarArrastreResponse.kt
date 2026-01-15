package com.example.myapplication.model.operador_grua

import com.google.gson.annotations.SerializedName

data class GenerarArrastreResponse(
    @SerializedName("data")
    val data: List<GenerarArrastreAttributes>,
    @SerializedName("meta")
    val meta: PaginationMeta?
)

data class PaginationMeta(
    @SerializedName("pagination")
    val pagination: PaginationData?
)

data class PaginationData(
    @SerializedName("page")
    val page: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("total")
    val total: Int
)

data class GenerarArrastreAttributes(
    @SerializedName("id")
    val id: Int,

    @SerializedName("documentId")  // ¡NUEVO! Necesario para Strapi v5
    val documentId: String,

    @SerializedName("folio")
    val folio: String,

    @SerializedName("observaciones")
    val observaciones: String,

    @SerializedName("ubicacion_arrastre")
    val ubicacion_arrastre: String,

    @SerializedName("estatus")
    val estatus: Int,

    @SerializedName("infraccion_id")
    val infraccion_id: InfraccionData?
)


// Modelo completo basado en el JSON
data class InfraccionData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("documentId")
    val documentId: String?,

    @SerializedName("folio")
    val folio: String,

    @SerializedName("placa_vehiculo")
    val placa_vehiculo: String,

    @SerializedName("fecha_infraccion")
    val fecha_infraccion: String,

    @SerializedName("ubicacion_infraccion")
    val ubicacion_infraccion: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("oficial_id")
    val oficial_id: String,  // Cambiado de PopulatedOficial a String

    @SerializedName("medio_infraccion")
    val medio_infraccion: String,

    @SerializedName("validacion")
    val validacion: Int
)