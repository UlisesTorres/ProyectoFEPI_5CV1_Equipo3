package com.example.myapplication.presenter.operador_grua

import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.view.operador_grua.DetalleSolicitudContract

class DetalleSolicitudPresenter(
    private var view: DetalleSolicitudContract.View?,
    private val model: SolicitudArrastreModel
) : DetalleSolicitudContract.Presenter {

    // --- CORRECCIÓN: La firma de la función ahora coincide con el contrato ---
    override fun aceptarSolicitud(idArrastre: Int, operadorGrua: String, gruaIdentificador: String) {
        view?.mostrarCargando()
        // --- CORRECCIÓN: Pasamos los nuevos parámetros al modelo ---
        model.aceptarSolicitudDeArrastre(idArrastre, operadorGrua, gruaIdentificador) { exito ->
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
