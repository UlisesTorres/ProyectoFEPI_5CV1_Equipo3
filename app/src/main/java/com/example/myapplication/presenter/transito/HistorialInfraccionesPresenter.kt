package com.example.myapplication.presenter.transito

import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.view.transito.HistorialInfraccionesContract

class HistorialInfraccionesPresenter(
    private var view: HistorialInfraccionesContract.View?,
    private val model: HistorialInfraccionesModel
) : HistorialInfraccionesContract.Presenter {

    override fun obtenerInfraccionesDelOficial() {
        view?.mostrarCargando()
        model.consultarInfracciones { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                view?.mostrarListaInfracciones(lista)
            } else {
                view?.mostrarError("No se pudieron cargar las infracciones")
            }
        }
    }

    override fun alSeleccionarInfraccion(folio: String) {
        view?.navegarADetalleInfraccion(folio)
    }

    override fun destruir() {
        view = null
    }
}