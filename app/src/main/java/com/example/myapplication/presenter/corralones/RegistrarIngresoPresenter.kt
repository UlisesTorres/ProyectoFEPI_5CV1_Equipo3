package com.example.myapplication.presenter.corralones

import com.example.myapplication.model.corralones.RegistrarIngresoModel
import com.example.myapplication.view.corralones.RegistrarIngresoContract

class RegistrarIngresoPresenter(
    private var view: RegistrarIngresoContract.View?,
    private val model: RegistrarIngresoModel
) : RegistrarIngresoContract.Presenter {

    override fun buscarFolio(folio: String) {
        if (folio.isEmpty()) {
            view?.mostrarError("El folio no puede estar vacío")
            return
        }

        view?.mostrarCargando()
        model.consultarFolioEnApi(folio) { placa, exito ->
            view?.ocultarCargando()
            if (exito && placa != null) {
                view?.llenarDatosVehiculo(placa)
                view?.habilitarRegistro(true)
            } else {
                view?.mostrarError("Folio no encontrado en el sistema")
                view?.habilitarRegistro(false)
            }
        }
    }

    override fun registrarIngreso(folio: String, placa: String, detalles: String) {
        // Lógica para guardar el ingreso en la base de datos
    }

    override fun destruir() {
        view = null
    }
}