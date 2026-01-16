package com.example.myapplication.view.corralones

interface RegistrarIngresoContract {
    interface View {
        fun mostrarCargando()
        fun ocultarCargando()
        fun llenarDatosVehiculo(datos: String)
        fun mostrarError(mensaje: String)
        fun habilitarRegistro(habilitar: Boolean)
        fun limpiarFormulario()
    }

    interface Presenter {
        fun buscarFolio(folio: String)
        fun registrarIngreso(nombreCorralon: String, direccionCorralon: String)
        fun destruir()
    }
}
