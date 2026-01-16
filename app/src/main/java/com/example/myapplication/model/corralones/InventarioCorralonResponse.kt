package com.example.myapplication.model.corralones

import com.example.myapplication.model.transito.InfraccionData
import com.google.gson.annotations.SerializedName

data class InventarioCorralonResponse(
    @SerializedName("data")
    val data: List<InventarioData>? = null
)

data class InventarioData(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("documentId") val documentId: String? = null,
    @SerializedName("nombre_corralon") val nombre_corralon: String? = null,
    @SerializedName("direccion_corralon") val direccion_corralon: String? = null,
    @SerializedName("infraccion_id") val infraccion: InfraccionData? = null,
    @SerializedName("pago_detalles") val pago: PagoInfo? = null // Añadimos info del pago si existe
)

data class PagoInfo(
    @SerializedName("estatus") val estatus: String? = null // pendiente, pagado, vencido
)
