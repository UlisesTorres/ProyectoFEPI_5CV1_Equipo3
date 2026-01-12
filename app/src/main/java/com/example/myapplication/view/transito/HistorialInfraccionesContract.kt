package com.example.myapplication.view.transito

import com.example.myapplication.model.transito.InfraccionAttributes

interface HistorialInfraccionesContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarListaInfracciones(infracciones: List<InfraccionAttributes>)
        fun mostrarError(mensaje: String)
        fun navegarADetalleInfraccion(folio: String)
    }

    interface Presenter {
        fun obtenerInfraccionesDelOficial()
        fun alSeleccionarInfraccion(folio: String)
        fun destruir()
    }
}