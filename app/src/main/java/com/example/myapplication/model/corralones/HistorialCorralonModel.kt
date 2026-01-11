package com.example.myapplication.model.corralones

class HistorialCorralonModel {
    fun consultarMovimientos(callback: (List<String>?, Boolean) -> Unit) {
        // Simulación de respuesta del servidor de corralones
        val datosFake = listOf(
            "INGRESO - Placa: ABC-123 - Fecha: 10/01/2026",
            "SALIDA  - Placa: XYZ-987 - Fecha: 09/01/2026",
            "INGRESO - Placa: GTO-456 - Fecha: 08/01/2026"
        )
        // Mañana se conectará con RetrofitClient
        callback(datosFake, true)
    }
}