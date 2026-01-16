package com.example.myapplication.model.corralones

import com.google.gson.annotations.SerializedName

data class PagosResponse(
    @SerializedName("data")
    val data: List<PagoData>? = null
)

data class PagoData(
    @SerializedName("id") val id: Int?,
    @SerializedName("attributes") val attributes: PagoAttributes?
)

data class PagoAttributes(
    @SerializedName("monto") val monto: Double?,
    @SerializedName("estatus") val estatus: String?, // ej: "Pagado", "Pendiente"
    @SerializedName("fecha_pago") val fecha_pago: String?
)
