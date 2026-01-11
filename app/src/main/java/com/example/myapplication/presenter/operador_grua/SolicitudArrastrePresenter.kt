package com.example.myapplication.presenter.operador_grua

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

    override fun aceptarSolicitud(idSolicitud: String) {
        view?.mostrarCargando()
        model.marcarComoAceptada(idSolicitud) { exito ->
            view?.ocultarCargando()
            if (exito) {
                view?.confirmarAceptacion(idSolicitud)
            } else {
                view?.mostrarError("No se pudo aceptar la solicitud, intenta de nuevo")
            }
        }
    }

    override fun destruir() {
        view = null
    }
}