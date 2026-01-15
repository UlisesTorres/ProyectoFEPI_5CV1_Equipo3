package com.example.myapplication.view.operador_grua

interface DetalleSolicitudContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarConfirmacion(mensaje: String)
        fun mostrarError(mensaje: String)
        fun cerrarVista()
    }

    interface Presenter {
        // Añade documentId
        fun aceptarSolicitud(
            idArrastre: Int,
            documentId: String,
            idInfraccion: Int,
            operadorGrua: String,
            gruaIdentificador: String
        )
        fun destruir()
    }
}