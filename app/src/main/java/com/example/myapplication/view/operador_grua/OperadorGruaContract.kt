package com.example.myapplication.view.operador_grua

interface OperadorGruaContract {
    interface View {
        fun navegarAArrastreEnCurso()
        fun navegarASolicitudes()
        fun navegarAHistorial()
        fun navegarAConfiguracion()
    }

    interface Presenter {
        fun clickArrastre()
        fun clickSolicitudes()
        fun clickHistorial()
        fun clickConfiguracion()
        fun destruir()
    }
}