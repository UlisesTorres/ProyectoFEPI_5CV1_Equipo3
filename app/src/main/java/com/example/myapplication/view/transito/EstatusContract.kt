package com.example.myapplication.view.transito

interface EstatusContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarEstatus(mensaje: String, esValido: Boolean)
        fun mostrarError(error: String)
    }

    interface Presenter {
        fun consultarPlaca(placa: String)
        fun destruir()
    }
}