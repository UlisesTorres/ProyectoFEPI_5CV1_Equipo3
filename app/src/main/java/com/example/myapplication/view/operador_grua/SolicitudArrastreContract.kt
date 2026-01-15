package com.example.myapplication.view.operador_grua

import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes

interface SolicitudArrastreContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarSolicitudes(lista: List<GenerarArrastreAttributes>)
        fun mostrarError(mensaje: String)
        fun confirmarAceptacion(mensaje: String)
    }

    interface Presenter {
        fun cargarSolicitudesNuevas()
        fun aceptarSolicitud(solicitud: GenerarArrastreAttributes)  // Esta es la firma esperada
        fun destruir()
    }
}