package com.example.myapplication.view.configuracion

interface ConfiguracionContract {

    interface View {
        fun navegarAlLogin()
        fun mostrarMensaje(mensaje: String)
        fun mostrarConfirmacionCierre()

        // ✅ NUEVOS
        fun mostrarUsuario(nombre: String, correo: String, rol: String)
        fun mostrarEstadoServidor(conectado: Boolean)
    }

    interface Presenter {
        fun clickCerrar()
        fun confirmarCierreSesion()

        // ✅ NUEVOS
        fun cargarDatosUsuario()
        fun verificarServidor()
        fun sincronizar()
    }
}