package com.example.myapplication.view.transito

import org.maplibre.android.geometry.LatLng

interface InfraccionesContract {
    interface View {
        fun actualizarDireccionEnPantalla(direccion: String)
        fun moverMarcador(latLng: LatLng)
        fun mostrarMensaje(mensaje: String)
        fun limpiarFormulario()
        fun bloquearEnvio()
        fun mostrarFechaActual(fecha: String)
        fun navegarAEvidencia(placas: String, direccion: String)
        fun ocultarTeclado()
    }

    interface Presenter {
        fun cargarFechaInfraccion()
        fun procesarClickMapa(punto: LatLng)
        fun validarYGuardarInfraccion(placas: String, infraccionTipo: String)
    }
}