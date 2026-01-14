package com.example.myapplication.model.transito


// 1. La respuesta principal que recibe Retrofit
data class EvidenciaResponse(
    val data: List<EvidenciaData>
)

// 2. El objeto que contiene el ID y los Atributos
data class EvidenciaData(
    val id: Int,
    val attributes: EvidenciaAttributes
)

// 3. Los datos reales que configuraste en Strapi
data class EvidenciaAttributes(
    val folio: String,
    val placa: String,
    val motivo: String,
    val fecha: String? = null
)