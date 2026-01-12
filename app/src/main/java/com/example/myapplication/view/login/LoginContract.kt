package com.example.myapplication.view.login

interface LoginContract {
    interface View {
        fun loginExitoso(
                token: String,
                userId: Int,
                roleName: String,
                username: String,
                email: String
            )
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