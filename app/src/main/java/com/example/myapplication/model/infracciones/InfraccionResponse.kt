package com.example.myapplication.model.infracciones


// 1. La respuesta principal que recibe Retrofit
data class InfraccionResponse(
    val data: List<InfraccionData>
)

// 2. El objeto que contiene el ID y los Atributos
data class InfraccionData(
    val id: Int,
    val attributes: InfraccionAttributes
)

// 3. Los datos reales que configuraste en Strapi
data class InfraccionAttributes(
    val folio: String,
    val placa: String,
    val motivo: String,
    val fecha: String? = null
)