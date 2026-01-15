package com.example.myapplication.model.transito

import com.google.gson.annotations.SerializedName

/**
 * Modelo FLAT (Plano) para coincidir con la respuesta transformada de Strapi.
 */
data class InfraccionesResponse(
    @SerializedName("data")
    val data: List<InfraccionData>? = null
)

data class InfraccionData(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("folio")
    val folio: String? = null,

    @SerializedName("placa_vehiculo")
    val placa_vehiculo: String? = null,

    @SerializedName("fecha_infraccion")
    val fecha_infraccion: String? = null,

    @SerializedName("ubicacion_infraccion")
    val ubicacion_infraccion: String? = null,

    @SerializedName("validacion")
    val validacion: Int? = 0,

    @SerializedName("evidencia_infraccion")
    val evidencia: Any? = null,

    @SerializedName("firma_infractor")
    val firma: Any? = null
)
