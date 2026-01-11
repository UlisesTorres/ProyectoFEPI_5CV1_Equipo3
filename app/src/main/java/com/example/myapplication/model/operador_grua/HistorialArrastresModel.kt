package com.example.myapplication.model.operador_grua

class HistorialArrastresModel {
    fun obtenerHistorialDesdeServidor(callback: (List<String>?, Boolean) -> Unit) {
        // Simulación de datos históricos
        val historialFake = listOf(
            "Arrastre Folio: 1001 - Placa: ABC-123",
            "Arrastre Folio: 1002 - Placa: DEF-456",
            "Arrastre Folio: 1003 - Placa: GHI-789"
        )
        // Mañana cambiaremos esto por la llamada real a Retrofit
        callback(historialFake, true)
    }
}