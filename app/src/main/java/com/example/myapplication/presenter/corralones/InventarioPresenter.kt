package com.example.myapplication.presenter.corralones

import com.example.myapplication.model.corralones.InventarioModel
import com.example.myapplication.view.corralones.InventarioContract

class InventarioPresenter(
    private var view: InventarioContract.View?,
    private val model: InventarioModel
) : InventarioContract.Presenter {

    override fun cargarInventario() {
        view?.mostrarCargando()
        model.obtenerAutosAlmacenados { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                if (lista.isEmpty()) {
                    view?.mostrarError("El corralón está vacío.")
                } else {
                    view?.mostrarVehiculosEnInventario(lista)
                }
            } else {
                view?.mostrarError("Error al obtener el inventario")
            }
        }
    }

    override fun alSeleccionarVehiculo(id: String) {
        view?.navegarADetalleVehiculo(id)
    }

    override fun destruir() {
        view = null
    }
}