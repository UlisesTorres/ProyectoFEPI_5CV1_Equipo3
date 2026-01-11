package com.example.myapplication.model.corralones

class InventarioModel {
    fun obtenerAutosAlmacenados(callback: (List<String>?, Boolean) -> Unit) {
        // Simulación de autos actualmente en el corralón
        val inventarioFake = listOf(
            "Placa: TSM-123 | Marca: Nissan | Color: Blanco",
            "Placa: URY-456 | Marca: Ford | Color: Rojo",
            "Placa: VBT-789 | Marca: VW | Color: Gris"
        )
        // Mañana integraremos Retrofit aquí
        callback(inventarioFake, true)
    }
}