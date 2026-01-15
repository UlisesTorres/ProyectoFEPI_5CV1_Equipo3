package com.example.myapplication.model.corralones

class InventarioModel {
    fun obtenerAutosAlmacenados(callback: (List<VehiculoInventario>?, Boolean) -> Unit) {
        // Datos de prueba basados en los requerimientos del chat
        val inventarioFake = listOf(
            VehiculoInventario(
                id = "1",
                folio = "ARR-7721",
                placa = "TSM-1234",
                estatus = "Retenido",
                fechaIngreso = "2024-01-12",
                ubicacion = "Patio A - Fila 3",
                observaciones = "Falla mecánica, sin llanta de refacción"
            ),
            VehiculoInventario(
                id = "2",
                folio = "ARR-8842",
                placa = "URY-5678",
                estatus = "En Espera",
                fechaIngreso = "2024-01-14",
                ubicacion = "Patio B - Fila 1",
                observaciones = "Espejo lateral derecho roto"
            ),
            VehiculoInventario(
                id = "3",
                folio = "ARR-9910",
                placa = "VBT-9012",
                estatus = "Listo para Entrega",
                fechaIngreso = "2024-01-15",
                ubicacion = "Zona de Salida",
                observaciones = "Ninguna"
            )
        )
        // Simulamos que la respuesta fue exitosa
        callback(inventarioFake, true)
    }
}
