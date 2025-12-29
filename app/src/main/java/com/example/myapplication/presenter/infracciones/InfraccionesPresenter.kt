package com.example.myapplication.presenter.infracciones

// IMPORTANTE: Cambiamos el LatLng de Google por el de MapLibre
import org.maplibre.android.geometry.LatLng
import com.example.myapplication.model.infracciones.InfraccionesModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface InfraccionesContract {
    interface View {
        fun actualizarDireccionEnPantalla(direccion: String)
        fun moverMarcador(latLng: LatLng)
        fun mostrarMensaje(mensaje: String)
        fun bloquearEnvio()
        fun limpiarFormulario()
        fun mostrarFechaActual(fecha: String)
    }
}

// El constructor debe definirse así:
class InfraccionesPresenter(
    private val view: InfraccionesContract.View,
    private val model: InfraccionesModel
) {
    fun procesarClickMapa(punto: LatLng) {
        val direccion = model.obtenerDireccion(punto)
        view.actualizarDireccionEnPantalla(direccion)
        view.moverMarcador(punto)
    }

    fun intentarGuardarInfraccion(firmaVacia: Boolean, contador: Int) {
        if (firmaVacia) {
            view.mostrarMensaje("Firma requerida")
            return
        }
        if (contador >= 5) {
            view.mostrarMensaje("Límite alcanzado. Sincronice.")
            view.bloquearEnvio()
            return
        }
    }

    fun cargarFechaInfraccion() {
        val formato = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val fechaHoy = formato.format(Date())
        view.mostrarFechaActual(fechaHoy)
    }
}