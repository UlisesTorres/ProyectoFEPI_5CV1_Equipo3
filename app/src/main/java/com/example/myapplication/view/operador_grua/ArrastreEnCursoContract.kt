package com.example.myapplication.view.operador_grua

interface ArrastreEnCursoContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarDetallesArrastre(folio: String, placa: String, destino: String)
        fun mostrarError(mensaje: String)
        fun finalizarServicioExitoso()
    }

    interface Presenter {
        fun obtenerServicioActivo()
        fun terminarArrastre(idServicio: String)
        fun destruir()
    }
}