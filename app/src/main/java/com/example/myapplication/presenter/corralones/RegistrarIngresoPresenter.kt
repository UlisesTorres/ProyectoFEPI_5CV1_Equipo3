package com.example.myapplication.presenter.corralones

import com.example.myapplication.model.corralones.RegistrarIngresoModel
import com.example.myapplication.view.corralones.RegistrarIngresoContract

class RegistrarIngresoPresenter(
    private var view: RegistrarIngresoContract.View?,
    private val model: RegistrarIngresoModel
) : RegistrarIngresoContract.Presenter {

    private var currentInfraccionDocumentId: String? = null

    override fun buscarFolio(folio: String) {
        if (folio.isEmpty()) {
            view?.mostrarError("El folio no puede estar vacío")
            return
        }

        view?.mostrarCargando()
        model.consultarFolioEnApi(folio) { id, docId, placa, marcaModelo, exito ->
            if (exito && docId != null) {
                model.verificarSiYaEstaEnCorralon(docId) { yaExiste ->
                    view?.ocultarCargando()
                    if (yaExiste) {
                        view?.mostrarError("⚠️ Este vehículo YA se encuentra en el corralón.")
                        view?.habilitarRegistro(false)
                    } else {
                        currentInfraccionDocumentId = docId
                        // Enviamos la placa a la vista para que se muestre en el campo deshabilitado
                        view?.llenarDatosVehiculo(placa ?: "Sin placa") 
                        view?.habilitarRegistro(true)
                    }
                }
            } else {
                view?.ocultarCargando()
                currentInfraccionDocumentId = null
                view?.mostrarError("Folio no encontrado en el sistema")
                view?.habilitarRegistro(false)
            }
        }
    }

    override fun registrarIngreso(nombreCorralon: String, direccionCorralon: String) {
        val docId = currentInfraccionDocumentId
        if (docId == null) {
            view?.mostrarError("Primero debes buscar un folio válido")
            return
        }

        if (nombreCorralon.isEmpty() || direccionCorralon.isEmpty()) {
            view?.mostrarError("Nombre y Dirección son obligatorios")
            return
        }

        view?.mostrarCargando()
        model.registrarIngreso(docId, nombreCorralon, direccionCorralon) { exito ->
            view?.ocultarCargando()
            if (exito) {
                view?.mostrarError("¡Vehículo ingresado al corralón con éxito!")
                view?.limpiarFormulario()
                view?.habilitarRegistro(false)
            } else {
                view?.mostrarError("Error al registrar en Strapi. Verifica tu conexión.")
            }
        }
    }

    override fun destruir() {
        view = null
    }
}
