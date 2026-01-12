package com.example.myapplication.view.transito

import com.example.myapplication.model.transito.InfraccionAttributes

interface SeleccionarInfraccionContract {
    interface View {            fun mostrarInfraccionesRecientes(infracciones: List<InfraccionAttributes>)
        fun mostrarError(mensaje: String)
        fun navegarAOrdenArrastre(infraccion: InfraccionAttributes)
    }

    interface Presenter {
        fun obtenerInfraccionesRecientes()
        fun seleccionarInfraccion(infraccion: InfraccionAttributes)
        fun destruir()
    }
}
    