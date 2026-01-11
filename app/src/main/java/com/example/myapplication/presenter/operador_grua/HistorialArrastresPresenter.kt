package com.example.myapplication.presenter.operador_grua

import com.example.myapplication.model.operador_grua.HistorialArrastresModel
import com.example.myapplication.view.operador_grua.HistorialArrastresContract

class HistorialArrastresPresenter(
    private var view: HistorialArrastresContract.View?,
    private val model: HistorialArrastresModel
) : HistorialArrastresContract.Presenter {

    override fun cargarHistorial() {
        view?.mostrarCargando()
        model.obtenerHistorialDesdeServidor { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                if (lista.isEmpty()) {
                    view?.mostrarError("No tienes arrastres registrados aún.")
                } else {
                    view?.mostrarHistorial(lista)
                }
            } else {
                view?.mostrarError("Error al conectar con el servidor")
            }
        }
    }

    override fun alSeleccionarArrastre(id: String) {
        view?.navegarADetalle(id)
    }

    override fun destruir() {
        view = null
    }
}