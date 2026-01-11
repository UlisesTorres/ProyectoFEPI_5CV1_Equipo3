package com.example.myapplication.view.transito

interface TransitoContract {
    interface View {
        fun navegarAGenerarInfraccion()
        fun navegarAConfiguracion()
        fun navegarAGenerarArrastre()
        fun navegarAHistorial()
    }

    interface Presenter {
        fun clickGenerarInfraccion()
        fun clickConfiguracion()
        fun clickGenerarArrastre()
        fun clickHistorial()
    }
}