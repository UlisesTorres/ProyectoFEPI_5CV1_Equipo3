package com.example.myapplication.view.corralones

interface InventarioContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarVehiculosEnInventario(vehiculos: List<String>)
        fun mostrarError(mensaje: String)
        fun navegarADetalleVehiculo(idVehiculo: String)
    }

    interface Presenter {
        fun cargarInventario()
        fun alSeleccionarVehiculo(id: String)
        fun destruir()
    }
}