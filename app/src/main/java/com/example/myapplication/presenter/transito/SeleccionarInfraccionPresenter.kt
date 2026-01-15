package com.example.myapplication.presenter.transito

import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.model.transito.InfraccionData
import com.example.myapplication.view.transito.SeleccionarInfraccionContract

class SeleccionarInfraccionPresenter(
    private var view: SeleccionarInfraccionContract.View?,
    private val model: HistorialInfraccionesModel
) : SeleccionarInfraccionContract.Presenter {

    override fun obtenerInfraccionesRecientes() {
        model.consultarInfracciones { lista, exito ->
            if (exito && lista != null) {
                view?.mostrarInfraccionesRecientes(lista)
            } else {
                view?.mostrarError("No se pudieron cargar las infracciones.")
            }
        }
    }

    override fun seleccionarInfraccion(infraccion: InfraccionData) {
        view?.navegarAOrdenArrastre(infraccion)
    }

    override fun destruir() {
        view = null
    }
}
