package com.example.myapplication.model.parquimetros

class EstatusModel {
    // Aquí simulamos o llamamos a Retrofit
    fun buscarPlacaEnServidor(placa: String, callback: (String?, Boolean) -> Unit) {
        // Implementar llamada real a RetrofitClient.apiService Por ahora simulamos una respuesta
        if (placa.isNotEmpty()) {
            callback("Placa $placa con tiempo vigente", true)
        } else {
            callback("Error: Placa no encontrada", false)
        }
    }
}