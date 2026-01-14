package com.example.myapplication.presenter.transito

import com.example.myapplication.model.transito.EvidenciaModel
import com.example.myapplication.view.transito.InfraccionesContract
import org.maplibre.android.geometry.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InfraccionesPresenter(
    private val view: InfraccionesContract.View,
    private val model: EvidenciaModel,
    private var direccionActual: String? = null,
    private var fechaISO: String = ""
) : InfraccionesContract.Presenter { // <--- Es vital que herede del Contrato

    override fun cargarFechaInfraccion() {
        val ahora = Date()

        // 🔹 PARA STRAPI (ISO 8601)
        // En InfraccionesPresenter.kt
        val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdfISO.timeZone = java.util.TimeZone.getTimeZone("UTC")
        fechaISO = sdfISO.format(ahora) // Esto genera: 2026-01-11T20:06:51.720Z

        // 🔹 PARA LA UI (bonito)
        val sdfUI = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaUI = sdfUI.format(ahora)

        view.mostrarFechaActual(fechaUI)
    }

    override fun procesarClickMapa(punto: LatLng) {
        view.moverMarcador(punto)
        direccionActual = model.obtenerDireccion(punto)
        view.actualizarDireccionEnPantalla(direccionActual!!)
    }

    override fun validarYGuardarInfraccion(placas: String, infraccionTipo: String) {
        if (placas.isEmpty()) {
            view.mostrarMensaje("¡Error! Debes ingresar las placas antes de continuar")
            return
        }

        if (direccionActual.isNullOrEmpty()) {
            view.mostrarMensaje("Debes seleccionar la ubicación en el mapa")
            return
        }

        view.ocultarTeclado()

        // ✅ AHORA SÍ, LA REAL
        view.navegarAEvidencia(placas, direccionActual!!, fechaISO)
    }

    override fun cargarCatalogoInfracciones() {
        model.obtenerCatalogoInfracciones(
            onSuccess = { listaDto -> // Recibes la lista de objetos DTO
                // ✅ PASA LA LISTA COMPLETA DIRECTAMENTE A LA VISTA
                // Ya no conviertas a nombres aquí.
                view.mostrarCatalogoInfracciones(listaDto)
            },
            onError = { mensajeError ->
                view.mostrarMensaje(mensajeError)
            }
        )
    }

}