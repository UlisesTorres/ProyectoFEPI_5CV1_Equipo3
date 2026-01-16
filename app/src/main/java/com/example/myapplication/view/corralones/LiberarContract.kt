package com.example.myapplication.view.corralones

import com.example.myapplication.model.corralones.VehiculoInventario

interface LiberarContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun mostrarVehiculosParaLiberar(vehiculos: List<VehiculoInventario>)
        fun mostrarError(mensaje: String)
        fun removerVehiculoDeLista(documentId: String)
    }

    interface Presenter {
        fun cargarVehiculos()
        fun liberarVehiculo(documentId: String)
        fun destruir()
    }
}
