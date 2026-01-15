package com.example.myapplication.view.transito

import com.example.myapplication.model.transito.InfraccionData

interface HistorialInfraccionesContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarListaInfracciones(infracciones: List<InfraccionData>)
        fun mostrarError(mensaje: String)
        fun navegarADetalleInfraccion(infraccion: InfraccionData)
    }

    interface Presenter {
        fun obtenerInfraccionesDelOficial()
        fun alSeleccionarInfraccion(infraccion: InfraccionData)
        fun destruir()
    }
}
