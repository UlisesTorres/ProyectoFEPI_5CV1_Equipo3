package com.example.myapplication.model.corralones

class RegistrarIngresoModel {
    fun consultarFolioEnApi(folio: String, callback: (String?, Boolean) -> Unit) {
        // Simulación de búsqueda en API
        if (folio == "12345") {
            callback("ABC-123-MEX", true)
        } else {
            callback(null, false)
        }
    }
}