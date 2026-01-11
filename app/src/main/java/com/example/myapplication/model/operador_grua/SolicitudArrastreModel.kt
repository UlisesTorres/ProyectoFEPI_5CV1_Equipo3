package com.example.myapplication.model.operador_grua

class SolicitudArrastreModel {
    fun obtenerPeticionesPendientes(callback: (List<String>?, Boolean) -> Unit) {
        // Simulación de peticiones mandadas por policías desde la calle
        val solicitudesFake = listOf(
            "SOLICITUD: #554 - Placa: ABC-123 - Ubicación: Av. Reforma 10",
            "SOLICITUD: #555 - Placa: MEX-990 - Ubicación: Calle Juárez 45"
        )
        // Mañana conectamos con la nueva API de Strapi
        callback(solicitudesFake, true)
    }

    fun marcarComoAceptada(id: String, callback: (Boolean) -> Unit) {
        // Lógica para avisar al servidor que esta grúa ya va en camino
        callback(true)
    }
}