package com.example.myapplication.view.transito

// Asegúrate de que esta importación esté presente
import com.example.myapplication.model.transito.InfraccionAttributes

interface HistorialInfraccionesContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarListaInfracciones(infracciones: List<InfraccionAttributes>)
        fun mostrarError(mensaje: String)

        // --- CORRECCIÓN ---
        // Ahora la vista debe poder recibir el objeto completo para la navegación.
        fun navegarADetalleInfraccion(infraccion: InfraccionAttributes)
    }

    interface Presenter {
        fun obtenerInfraccionesDelOficial()
        // --- CORRECCIÓN ---
        // El presenter ahora manejará el objeto completo.
        fun alSeleccionarInfraccion(infraccion: InfraccionAttributes)

        fun destruir()
    }
}
