package com.example.myapplication.model.corralones

import com.google.gson.annotations.SerializedName

data class CorralonSitioResponse(
    @SerializedName("data")
    val data: List<CorralonSitioData>? = null
)

data class CorralonSitioData(
    @SerializedName("id") val id: Int?,
    @SerializedName("attributes") val attributes: CorralonSitioAttributes?
)

data class CorralonSitioAttributes(
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("direccion") val direccion: String?
)
