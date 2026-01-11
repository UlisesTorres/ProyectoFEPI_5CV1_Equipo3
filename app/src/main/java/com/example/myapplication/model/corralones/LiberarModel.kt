package com.example.myapplication.model.corralones

class LiberarModel {
    fun consultarVehiculoAlmacenado(id: String, callback: (String?, Boolean) -> Unit) {
        // Mañana conectamos con la nueva API
        if (id.isNotEmpty()) {
            callback("Vehículo: VW Jetta | Placa: XYZ-123 | Estatus: Apto para liberación", true)
        } else {
            callback(null, false)
        }
    }

    fun registrarSalida(id: String, callback: (Boolean) -> Unit) {
        // Llamada a la API para cambiar estatus a 'LIBERADO'
        callback(true)
    }
}