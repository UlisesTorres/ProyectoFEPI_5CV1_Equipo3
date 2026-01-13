package com.example.myapplication.view.operador_grua

// --- CORRECCIÓN #1: Cambiamos la importación ---
// Ahora importamos la clase que realmente contiene los datos.
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes

interface SolicitudArrastreContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()

        // --- CORRECCIÓN #2: Actualizamos el tipo de la lista ---
        // La vista ahora debe poder mostrar una lista de 'GenerarArrastreAttributes'.
        fun mostrarSolicitudes(lista: List<GenerarArrastreAttributes>)

        fun mostrarError(mensaje: String)
        fun confirmarAceptacion(folio: String) // Lo dejamos por ahora
    }

    interface Presenter {
        fun cargarSolicitudesNuevas()

        // --- CORRECCIÓN #3: Actualizamos el tipo del parámetro ---
        // La acción de aceptar ahora recibirá el objeto con los datos directamente.
        fun aceptarSolicitud(solicitud: GenerarArrastreAttributes)

        fun destruir()
    }
}
