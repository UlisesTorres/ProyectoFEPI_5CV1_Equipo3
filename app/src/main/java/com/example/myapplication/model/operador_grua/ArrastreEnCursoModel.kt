package com.example.myapplication.model.operador_grua

class ArrastreEnCursoModel {
    fun consultarServicioActivo(callback: (Map<String, String>?, Boolean) -> Unit) {
        // Mañana aquí pondrás la llamada a la nueva API
        // Simulamos datos por ahora
        val datosFake = mapOf(
            "folio" to "INF-998877",
            "placa" to "XYZ-789-A",
            "destino" to "Corralón Zona Norte"
        )
        callback(datosFake, true)
    }
}