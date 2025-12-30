package com.example.myapplication.view.main

interface MainContract {
    interface View {
        fun navegarATransito()
        fun navegarAGruas()
        fun navegarASupervisor()
        fun navegarAGestor()
        fun mostrarError(mensaje: String)
    }

    interface Presenter {
        fun intentarLogin(usuario: String, pass: String)
    }
}