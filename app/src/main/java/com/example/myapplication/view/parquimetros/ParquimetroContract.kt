package com.example.myapplication.view.parquimetros

interface ParquimetroContract {
    interface View {
        fun navegarAStatus()
        fun navegarAConfiguracion()
        fun mostrarMensajeError(mensaje: String)
    }

    interface Presenter {
        fun alHacerClickConsultar()
        fun alHacerClickConfiguracion()
        fun destruir()
    }
}