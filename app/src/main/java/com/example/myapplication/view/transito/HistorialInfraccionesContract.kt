package com.example.myapplication.view.transito

interface HistorialInfraccionesContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarListaInfracciones(infracciones: List<String>)
        fun mostrarError(mensaje: String)
        fun navegarADetalleInfraccion(folio: String)
    }

    interface Presenter {
        fun obtenerInfraccionesDelOficial()
        fun alSeleccionarInfraccion(folio: String)
        fun destruir()
    }
}