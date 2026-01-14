package com.example.myapplication.model.vehiculo

data class VehiculoResponse(
    val data: List<VehiculoDTO>
)

data class VehiculoDTO(
    val id: Int,
    val placa_id: String?,
    val marca: String?,
    val modelo: String?,
    val color: String?,
    val tipo: String?
)
