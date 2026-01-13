package com.example.myapplication.presenter.operador_grua

import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.view.operador_grua.DetalleSolicitudContract

class DetalleSolicitudPresenter(
    private var view: DetalleSolicitudContract.View?,
    private val model: SolicitudArrastreModel
) : DetalleSolicitudContract.Presenter {

    override fun aceptarSolicitud(idArrastre: Int) {
        view?.mostrarCargando()
        model.aceptarSolicitudDeArrastre(idArrastre) { exito ->
            view?.ocultarCargando()
            if (exito) {
                view?.mostrarConfirmacion("Solicitud aceptada con éxito")
                view?.cerrarVista()
            } else {
                view?.mostrarError("No se pudo aceptar la solicitud")
            }
        }
    }

    override fun destruir() {
        view = null
    }
}
