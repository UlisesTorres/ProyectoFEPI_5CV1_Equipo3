package com.example.myapplication.model.configuracion

class ConfiguracionModel {
    fun limpiarDatosSesion(callback: (Boolean) -> Unit) {
        // Aquí iría la lógica para borrar SharedPreferences
        // o eliminar el Token de la base de datos local
        callback(true)
    }
}