package com.example.myapplication.view.corralones

interface HistorialCorralonContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarMovimientos(movimientos: List<String>)
        fun mostrarError(mensaje: String)
    }

    interface Presenter {
        fun obtenerHistorialCompleto()
        fun filtrarPorPlaca(placa: String)
        fun destruir()
    }
}