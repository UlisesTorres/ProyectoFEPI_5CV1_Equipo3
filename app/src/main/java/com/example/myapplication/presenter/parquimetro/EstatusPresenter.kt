package com.example.myapplication.presenter.parquimetro

import com.example.myapplication.model.parquimetros.EstatusModel
import com.example.myapplication.view.parquimetros.EstatusContract

class EstatusPresenter(
    private var view: EstatusContract.View?,
    private val model: EstatusModel
) : EstatusContract.Presenter {

    override fun consultarPlaca(placa: String) {
        view?.mostrarCargando()

        model.buscarPlacaEnServidor(placa) { resultado, esValido ->
            view?.ocultarCargando()
            if (esValido) {
                view?.mostrarEstatus(resultado!!, true)
            } else {
                view?.mostrarError(resultado!!)
            }
        }
    }

    override fun destruir() {
        view = null
    }
}