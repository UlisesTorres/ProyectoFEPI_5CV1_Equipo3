package com.example.myapplication.presenter.configuracion

import com.example.myapplication.model.configuracion.ConfiguracionModel
import com.example.myapplication.view.configuracion.ConfiguracionContract

class ConfiguracionPresenter(
    private var view: ConfiguracionContract.View?,
    private val model: ConfiguracionModel
) : ConfiguracionContract.Presenter {

    override fun clickCerrar() {
        view?.mostrarConfirmacionCierre()
    }

    override fun confirmarCierreSesion() {
        model.limpiarDatosSesion { exito ->
            if (exito) {
                view?.mostrarMensaje("Sesión cerrada correctamente")
                view?.navegarAlLogin()
            }
        }
    }

    // Importante: método para evitar memory leaks
    fun destruir() {
        view = null
    }
}