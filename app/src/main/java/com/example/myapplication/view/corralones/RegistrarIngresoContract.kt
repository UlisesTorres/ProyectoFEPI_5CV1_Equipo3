package com.example.myapplication.view.corralones

interface RegistrarIngresoContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun llenarDatosVehiculo(placa: String)
        fun mostrarError(mensaje: String)
        fun habilitarRegistro(habilitar: Boolean)
    }

    interface Presenter {
        fun buscarFolio(folio: String)
        fun registrarIngreso(folio: String, placa: String, detalles: String)
        fun destruir()
    }
}