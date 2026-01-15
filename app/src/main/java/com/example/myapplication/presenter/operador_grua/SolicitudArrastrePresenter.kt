package com.example.myapplication.presenter.operador_grua

import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes
import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.view.operador_grua.SolicitudArrastreContract

class SolicitudArrastrePresenter(
    private var view: SolicitudArrastreContract.View?,
    private val model: SolicitudArrastreModel
) : SolicitudArrastreContract.Presenter {

    override fun cargarSolicitudesNuevas() {
        view?.mostrarCargando()

        model.obtenerPeticionesPendientes { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                view?.mostrarSolicitudes(lista)
            } else {
                view?.mostrarError("No hay solicitudes de arrastre en este momento")
            }
        }
    }

    // CORRECCIÓN: Revisa cómo está definido en tu contrato
    // Si el contrato no tiene este mé
    // Si el contrato lo tiene con diferente firma, ajústala
    override fun aceptarSolicitud(solicitud: GenerarArrastreAttributes) {
        // Si el contrato requiere un objeto GenerarArrastreAttributes
        view?.mostrarCargando()

        model.aceptarSolicitudDeArrastre(
            idArrastre = solicitud.id,
            documentId = solicitud.documentId,
            idInfraccion = solicitud.infraccion_id?.id ?: -1,
            operadorGrua = "Operador Grua",  // Deberías obtener esto de SharedPreferences
            gruaIdentificador = "Grua-01"    // Deberías obtener esto de SharedPreferences
        ) { exito ->
            view?.ocultarCargando()
            if (exito) {
                view?.confirmarAceptacion("Solicitud ${solicitud.folio} aceptada")
            } else {
                view?.mostrarError("No se pudo aceptar la solicitud")
            }
        }
    }

    override fun destruir() {
        view = null
    }
}