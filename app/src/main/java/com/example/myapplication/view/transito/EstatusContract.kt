package com.example.myapplication.view.transito

import com.example.myapplication.model.transito.TipoResultado

interface EstatusContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarEstatus(mensaje: String, tipo: TipoResultado)
        fun mostrarError(error: String)
    }

    interface Presenter {
        fun consultarPlaca(placa: String)
        fun destruir()
    }
}