package com.example.myapplication.view.transito

interface TransitoContract {
    interface View {
        fun navegarAGenerarInfraccion()
        fun navegarAConfiguracion()
        fun navegarAGenerarArrastre()
        fun navegarAHistorial()
        fun navegarAParquimetros()
    }

    interface Presenter {
        fun clickGenerarInfraccion()
        fun clickConfiguracion()
        fun clickGenerarArrastre()
        fun clickHistorial()
        fun clickParquimetros()

    }
}