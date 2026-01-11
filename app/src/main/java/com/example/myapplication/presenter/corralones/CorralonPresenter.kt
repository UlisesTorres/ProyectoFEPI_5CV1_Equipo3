package com.example.myapplication.presenter.corralones

import com.example.myapplication.view.corralones.CorralonContract

class CorralonPresenter(private var view: CorralonContract.View?) : CorralonContract.Presenter {

    override fun alClickIngreso() {
        view?.navegarAIngreso()
    }

    override fun alClickInventario() {
        view?.navegarAInventario()
    }

    override fun alClickSalida() {
        view?.navegarALiberacion()
    }

    override fun alClickHistorial() {
        view?.navegarAHistorial()
    }

    override fun alClickConfiguracion() {
        view?.navegarAConfiguracion()
    }

    override fun destruir() {
        view = null
    }
}