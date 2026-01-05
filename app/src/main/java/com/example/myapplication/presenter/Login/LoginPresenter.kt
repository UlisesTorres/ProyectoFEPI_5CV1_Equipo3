package com.example.myapplication.presenter.Login

import com.example.myapplication.view.Login.MainContract

class LoginPresenter(private val view: MainContract.View) : MainContract.Presenter {

    override fun intentarLogin(usuario: String, pass: String) {
        when {
            usuario == "oficial" && pass == "123" -> view.navegarATransito()
            usuario == "gruero" && pass == "123" -> view.navegarAGruas()
            usuario == "super" && pass == "123" -> view.navegarASupervisor()
            usuario == "gestor" && pass == "123" -> view.navegarAGestor()
            else -> view.mostrarError("Usuario o contraseña incorrectos")
        }
    }
}