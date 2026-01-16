package com.example.myapplication.model.transito

import com.google.gson.annotations.SerializedName

data class InfraccionesResponse(
    @SerializedName("data")
    val data: List<InfraccionData>? = null
)

data class InfraccionData(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("documentId")
    val documentId: String?,

    @SerializedName("folio")
    val folio: String? = null,

    @SerializedName("placa_vehiculo")
    val placa_vehiculo: String? = null,

    @SerializedName("marca_vehiculo")
    val marca: String? = null,

    @SerializedName("modelo_vehiculo")
    val modelo: String? = null,

    @SerializedName("fecha_infraccion")
    val fecha_infraccion: String? = null,

    @SerializedName("ubicacion_infraccion")
    val ubicacion_infraccion: String? = null,

    @SerializedName("pago") 
    val pago: Any? = null,

    @SerializedName("pagos") // A veces Strapi pluraliza relaciones
    val pagos: Any? = null,

    @SerializedName("validacion")
    val validacion: Int? = 0,

    @SerializedName("evidencia_infraccion")
    val evidencia: Any? = null,

    @SerializedName("firma_infractor")
    val firma: Any? = null
)
