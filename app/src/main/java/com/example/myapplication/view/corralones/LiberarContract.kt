package com.example.myapplication.view.corralones

interface LiberarContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarDatosVehiculo(detalles: String)
        fun mostrarError(mensaje: String)
        fun confirmarSalidaExitosa()
    }

    interface Presenter {
        fun buscarVehiculoParaLiberar(identificador: String)
        fun procesarLiberacion(idVehiculo: String)
        fun destruir()
    }
}