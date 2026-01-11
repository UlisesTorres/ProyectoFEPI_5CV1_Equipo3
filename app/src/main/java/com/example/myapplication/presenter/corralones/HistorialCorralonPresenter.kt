package com.example.myapplication.presenter.corralones

import com.example.myapplication.model.corralones.HistorialCorralonModel
import com.example.myapplication.view.corralones.HistorialCorralonContract

class HistorialCorralonPresenter(
    private var view: HistorialCorralonContract.View?,
    private val model: HistorialCorralonModel
) : HistorialCorralonContract.Presenter {

    override fun obtenerHistorialCompleto() {
        view?.mostrarCargando()
        model.consultarMovimientos { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                view?.mostrarMovimientos(lista)
            } else {
                view?.mostrarError("No se pudo cargar el historial del corralón")
            }
        }
    }

    override fun filtrarPorPlaca(placa: String) {
        // Aquí podrías añadir lógica para buscar una placa específica en la lista
    }

    override fun destruir() {
        view = null
    }
}