package com.example.myapplication.view.operador_grua

interface HistorialArrastresContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        // Aquí pasaremos una lista de datos en el futuro
        fun mostrarHistorial(lista: List<String>)
        fun mostrarError(mensaje: String)
        fun navegarADetalle(idArrastre: String)
    }

    interface Presenter {
        fun cargarHistorial()
        fun alSeleccionarArrastre(id: String)
        fun destruir()
    }
}