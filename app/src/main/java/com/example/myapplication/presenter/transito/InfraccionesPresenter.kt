package com.example.myapplication.presenter.transito

import com.example.myapplication.model.infracciones.InfraccionesModel
import com.example.myapplication.view.transito.InfraccionesContract
import org.maplibre.android.geometry.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InfraccionesPresenter(
    private val view: InfraccionesContract.View,
    private val model: InfraccionesModel
) : InfraccionesContract.Presenter { // <--- Es vital que herede del Contrato

    override fun cargarFechaInfraccion() {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaActual = sdf.format(Date())
        view.mostrarFechaActual(fechaActual)
    }

    override fun procesarClickMapa(punto: LatLng) {
        view.moverMarcador(punto)
        val direccion = model.obtenerDireccion(punto)
        view.actualizarDireccionEnPantalla(direccion)
    }

    override fun validarYGuardarInfraccion(placas: String, infraccionTipo: String) {
        if (placas.isEmpty()) {
            view.mostrarMensaje("¡Error! Debes ingresar las placas antes de continuar")
            // No llamamos a navegar, por lo tanto, la app se queda ahí
            return
        }

        view.ocultarTeclado() // <--- Paso vital
        view.navegarAEvidencia(placas, "Dirección actual obtenida")
        // Si llegamos aquí, es que hay placas.
        // Pedimos la dirección (que ya tenemos guardada o la obtenemos de la vista)
        // Supongamos que pasamos la dirección actual:

    }
}