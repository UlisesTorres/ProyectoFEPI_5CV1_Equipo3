package com.example.myapplication.presenter.configuracion

import com.example.myapplication.view.configuracion.ConfiguracionContract

class ConfiguracionPresenter(private val view: ConfiguracionContract.View): ConfiguracionContract.Presenter {


    override fun clickCerrar() {
        view.mostrarConfirmacionCierre()
    }

    override fun confirmarCierreSesion() {
        view.mostrarMensaje("Se ha cerrado la Sesion Correctamente")
        view.navegarAlLogin()
    }


}