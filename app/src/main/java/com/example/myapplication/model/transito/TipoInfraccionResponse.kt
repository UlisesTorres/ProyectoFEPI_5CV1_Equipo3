package com.example.myapplication.model.transito

data class TipoInfraccionResponse(
    val data: List<TipoInfraccionDTO>
)

data class TipoInfraccionDTO(
    val id: Int,
    val clave: String?,
    val nombre: String?,
    val descripcion: String?,
    val articulo_id: List<ArticuloDTO>?  // Lista directa
)

data class ArticuloDTO(
    val id: Int,
    val ordenamiento: String?,
    val articulo_numero: Int?,
    val contenido: String?,
    val ambito: String?,
    val fecha_publicacion: String?,
    val fecha_ultima_reforma: String?
)
