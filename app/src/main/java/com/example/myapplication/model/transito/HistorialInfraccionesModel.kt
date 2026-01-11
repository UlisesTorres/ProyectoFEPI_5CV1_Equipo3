package com.example.myapplication.model.transito

class HistorialInfraccionesModel {
    fun consultarInfracciones(callback: (List<String>?, Boolean) -> Unit) {
        // Simulación de datos de Strapi
        val datosFake = listOf(
            "Folio: INF-001 | Placa: ABC-123 | Estatus: Pendiente",
            "Folio: INF-002 | Placa: MEX-456 | Estatus: Pagada",
            "Folio: INF-003 | Placa: GTO-789 | Estatus: En Proceso"
        )
        // Mañana usaremos RetrofitClient.infraccionApiService.getHistorial()
        callback(datosFake, true)
    }
}