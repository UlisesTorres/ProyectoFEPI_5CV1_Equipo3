package com.example.myapplication.view.transito

import com.example.myapplication.model.transito.InfraccionData

interface SeleccionarInfraccionContract {
    interface View {
        fun mostrarInfraccionesRecientes(infracciones: List<InfraccionData>)
        fun mostrarError(mensaje: String)
        fun navegarAOrdenArrastre(infraccion: InfraccionData)
    }

    interface Presenter {
        fun obtenerInfraccionesRecientes()
        fun seleccionarInfraccion(infraccion: InfraccionData)
        fun destruir()
    }
}
