package com.example.myapplication.view.corralones

interface CorralonContract {
    interface View {
        fun navegarAIngreso()
        fun navegarAInventario()
        fun navegarALiberacion()
        fun navegarAHistorial()
        fun navegarAConfiguracion()
    }

    interface Presenter {
        fun alClickIngreso()
        fun alClickInventario()
        fun alClickSalida()
        fun alClickHistorial()
        fun alClickConfiguracion()
        fun destruir()
    }
}