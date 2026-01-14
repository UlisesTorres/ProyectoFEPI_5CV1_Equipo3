package com.example.myapplication.model.transito

// ... (TipoInfraccionResponse y ArticuloDTO se quedan igual) ...

data class TipoInfraccionResponse(
    val data: List<TipoInfraccionDTO>
)

data class ArticuloDTO(
    val id: Int,
    val ordenamiento: String?,
    val articulo_numero: String?,
    val contenido: String?
)

/**
 * Representa la envoltura 'data' que Strapi usa para las relaciones.
 * --- ¡AQUÍ ESTÁ EL CAMBIO CLAVE! ---
 */
data class ArticuloRelation(
    // Antes era: val data: ArticuloDTO?
    // Ahora es:
    val data: List<ArticuloDTO>? // <-- ¡Ahora es una Lista!
)

/**
 * Representa una infracción. La definición del campo 'articulo' no cambia aquí,
 * porque el cambio real está dentro de ArticuloRelation.
 */
data class TipoInfraccionDTO(
    val id: Int,
    val clave: String?,
    val nombre: String?,
    val descripcion: String?,
    val articulo: ArticuloRelation?
)
