package com.example.myapplication.view.transito

import org.maplibre.android.geometry.LatLng
import com.example.myapplication.model.transito.TipoInfraccionDTO

interface InfraccionesContract {
    interface View {
        fun actualizarDireccionEnPantalla(direccion: String)
        fun moverMarcador(latLng: LatLng)
        fun mostrarMensaje(mensaje: String)
        fun limpiarFormulario()
        fun bloquearEnvio()
        fun mostrarFechaActual(fecha: String)
        fun navegarAEvidencia(placas: String, direccion: String, fechaISO: String)
        fun ocultarTeclado()
        fun mostrarCatalogoInfracciones(lista: List<TipoInfraccionDTO>)

    }

    interface Presenter {
        fun cargarFechaInfraccion()
        fun procesarClickMapa(punto: LatLng)
        fun validarYGuardarInfraccion(placas: String, infraccionTipo: String)
        fun cargarCatalogoInfracciones()
    }
}