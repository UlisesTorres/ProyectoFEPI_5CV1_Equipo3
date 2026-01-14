// LicenciaDTO.kt
package com.example.myapplication.model.licencia

data class LicenciaResponse(
    val data: List<LicenciaDTO>
)

data class LicenciaDTO(
    val id: Int,
    val num_licencia: String?,
    val nombre: String?,
    val apellido: String?,
    val estado_licencia: String?,
    val tipo_licencia: String?,
    val fecha_vencimiento: String?
)
