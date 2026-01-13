package com.example.myapplication.presenter.operador_grua

// --- CORRECCIÓN #1: Cambiamos la importación ---
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes
import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.view.operador_grua.SolicitudArrastreContract

class SolicitudArrastrePresenter(
    private var view: SolicitudArrastreContract.View?,
    private val model: SolicitudArrastreModel
) : SolicitudArrastreContract.Presenter {

    override fun cargarSolicitudesNuevas() {
        view?.mostrarCargando()

        // --- CORRECCIÓN #2: El tipo de 'lista' ahora es el correcto ---
        // El callback ahora nos da una lista de 'GenerarArrastreAttributes'.
        model.obtenerPeticionesPendientes { lista, exito ->
            view?.ocultarCargando()
            if (exito && lista != null) {
                // Pasamos la lista correcta a la vista.
                view?.mostrarSolicitudes(lista)
            } else {
                view?.mostrarError("No hay solicitudes de arrastre en este momento")
            }
        }
    }

    // --- CORRECCIÓN #3: Actualizamos el tipo del parámetro ---
    override fun aceptarSolicitud(solicitud: GenerarArrastreAttributes) {
        view?.mostrarError("Funcionalidad 'Aceptar' no implementada aún.")
    }

    override fun destruir() {
        view = null
    }
}
