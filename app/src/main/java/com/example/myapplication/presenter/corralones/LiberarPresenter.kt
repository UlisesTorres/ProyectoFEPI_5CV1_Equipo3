package com.example.myapplication.presenter.corralones

import com.example.myapplication.model.corralones.LiberarModel
import com.example.myapplication.view.corralones.LiberarContract

class LiberarPresenter(
    private var view: LiberarContract.View?,
    private val model: LiberarModel
) : LiberarContract.Presenter {

    override fun buscarVehiculoParaLiberar(identificador: String) {
        if (identificador.isEmpty()) {
            view?.mostrarError("Ingresa una placa o folio")
            return
        }
        view?.mostrarCargando()
        model.consultarVehiculoAlmacenado(identificador) { datos, exito ->
            view?.ocultarCargando()
            if (exito && datos != null) {
                view?.mostrarDatosVehiculo(datos)
            } else {
                view?.mostrarError("Vehículo no encontrado o ya liberado")
            }
        }
    }

    override fun procesarLiberacion(idVehiculo: String) {
        view?.mostrarCargando()
        model.registrarSalida(idVehiculo) { exito ->
            view?.ocultarCargando()
            if (exito) {
                view?.confirmarSalidaExitosa()
            } else {
                view?.mostrarError("Error al procesar la salida")
            }
        }
    }

    override fun destruir() {
        view = null
    }
}