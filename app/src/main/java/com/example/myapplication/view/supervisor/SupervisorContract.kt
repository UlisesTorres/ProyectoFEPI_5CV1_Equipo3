package com.example.myapplication.view.supervisor

interface SupervisorContract {
    interface View {
        fun navegarAConfiguracion()
    }

    interface Presenter {
        fun clickConfiguracion()
    }
}