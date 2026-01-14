package com.example.myapplication.model.transito

data class TipoInfraccionResponse(
    val data: List<TipoInfraccionDTO>
)

data class ArticuloDTO(
    val id: Int,
    val ordenamiento: String?,
    val articulo_numero: String?, // Strapi a veces usa guiones bajos en los nombres de campo
    val contenido: String?
    // Puedes añadir otros campos como 'ambito', 'fecha_publicacion', si los necesitas
)

data class ArticuloRelation(
    val data: ArticuloDTO?
)


data class TipoInfraccionDTO(
    val id: Int,
    val clave: String?,
    val nombre: String?,
    val descripcion: String?,

    // --- ESTA ES LA LÍNEA QUE AÑADIMOS ---
    // El nombre 'articulo' debe coincidir con el nombre de la relación en Strapi.
    val articulo: ArticuloRelation?
)
