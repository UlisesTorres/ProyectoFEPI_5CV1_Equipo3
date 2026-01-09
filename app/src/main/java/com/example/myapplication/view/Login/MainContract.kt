package com.example.myapplication.view.Login

interface MainContract {
    interface View {
        fun navegarATransito()
        fun navegarAGruas()
        fun navegarASupervisor()
        fun navegarAGestor()
        fun navegarAParquimetro()
        fun mostrarError(mensaje: String)
    }

    interface Presenter {
        fun intentarLogin(usuario: String, pass: String)
    }
}