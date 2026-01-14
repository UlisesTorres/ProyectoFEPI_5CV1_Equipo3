package com.example.myapplication.model.transito

data class TipoInfraccionResponse(
    val data: List<TipoInfraccionDTO>
)

data class TipoInfraccionDTO(
    val id: Int,
    val clave: String?,
    val nombre: String?,
    val descripcion: String?
)
