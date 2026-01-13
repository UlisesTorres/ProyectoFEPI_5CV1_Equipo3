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
        // --- CORRECCIÓN: La función ahora acepta los datos del operador ---
        fun aceptarSolicitud(idArrastre: Int, operadorGrua: String, gruaIdentificador: String)
        fun destruir()
    }
}
