package com.example.myapplication.view.configuracion

interface ConfiguracionContract {
    interface View{
        fun navegarAlLogin()
        fun mostrarMensaje(mensaje:String)
        fun mostrarConfirmacionCierre()
    }
    interface Presenter{
        fun clickCerrar()
        fun confirmarCierreSesion()
    }
}