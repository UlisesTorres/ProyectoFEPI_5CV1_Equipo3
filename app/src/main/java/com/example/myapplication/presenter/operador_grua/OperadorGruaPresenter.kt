package com.example.myapplication.presenter.operador_grua

import com.example.myapplication.view.operador_grua.OperadorGruaContract

class OperadorGruaPresenter(private var view: OperadorGruaContract.View?) : OperadorGruaContract.Presenter {

    override fun clickArrastre() {
        // Aquí se podría validar si realmente hay un arrastre activo en el Model
        view?.navegarAArrastreEnCurso()
    }

    override fun clickSolicitudes() {
        view?.navegarASolicitudes()
    }

    override fun clickHistorial() {
        view?.navegarAHistorial()
    }

    override fun clickConfiguracion() {
        view?.navegarAConfiguracion()
    }

    override fun destruir() {
        view = null
    }
}