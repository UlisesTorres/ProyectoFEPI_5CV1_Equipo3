package com.example.myapplication.presenter.corralones

import com.example.myapplication.model.corralones.LiberarModel
import com.example.myapplication.view.corralones.LiberarContract

class LiberarPresenter(
    private var view: LiberarContract.View?,
    private val model: LiberarModel
) : LiberarContract.Presenter {

    override fun cargarVehiculos() {
        view?.mostrarCargando()
        model.obtenerVehiculosParaLiberar { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                view?.mostrarVehiculosParaLiberar(lista)
            } else {
                view?.mostrarError("Error al cargar vehículos")
            }
        }
    }

    override fun liberarVehiculo(id: String) {
        view?.mostrarCargando()
        model.liberarVehiculo(id) { exito ->
            view?.ocultarCargando()
            if (exito) {
                view?.removerVehiculoDeLista(id)
                view?.mostrarError("✅ Vehículo liberado correctamente")
            } else {
                view?.mostrarError("❌ Error al liberar el vehículo")
            }
        }
    }

    override fun destruir() {
        view = null
    }
}
