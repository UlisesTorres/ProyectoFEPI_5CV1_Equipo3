package com.example.myapplication.view.login

interface LoginContract {
    interface View {
        // 🔹 Agrega esta línea para que el Presenter pueda pasar el Token a la Activity
        fun loginExitoso(token: String, userId: Int, roleType: String)
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