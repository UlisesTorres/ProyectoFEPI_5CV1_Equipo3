package com.example.myapplication.model.transito

import com.google.gson.annotations.SerializedName

/**
 * Representa el objeto contenedor para la petición POST,
 * como lo requiere Strapi.
 */
data class OrdenArrastreRequest(
    @SerializedName("data")
    val data: OrdenArrastreData
)

/**
 * Contiene los campos de datos específicos para la nueva entrada 'Generar_arrastre'.
 * Esta clase DEBE coincidir con la estructura de la tabla en Strapi.
 */
data class OrdenArrastreData(

    // --- CORRECCIÓN #1: Añadir el campo 'folio' ---
    // Este campo es requerido por tu nueva tabla 'Generar_arrastre'.
    @SerializedName("folio")
    val folio: String,

    // --- Este campo ya estaba correcto ---
    // La relación con la tabla 'Infraccion'.
    @SerializedName("infraccion_id")
    val infraccionId: Int,

    // --- CORRECCIÓN #2: El campo 'observaciones' ahora sí existe ---
    // Este campo también es requerido por tu nueva tabla.
    @SerializedName("observaciones")
    val observaciones: String,

    @SerializedName("estatus")
    val estatus: Int = 0,

    @SerializedName("ubicacion_arrastre")
    val ubicacion: String
)
