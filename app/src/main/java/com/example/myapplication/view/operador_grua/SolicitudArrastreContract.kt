package com.example.myapplication.view.operador_grua

interface SolicitudArrastreContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarSolicitudes(lista: List<String>)
        fun mostrarError(mensaje: String)
        fun confirmarAceptacion(folio: String)
    }

    interface Presenter {
        fun cargarSolicitudesNuevas()
        fun aceptarSolicitud(idSolicitud: String)
        fun destruir()
    }
}