package com.example.myapplication.view.parquimetros

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