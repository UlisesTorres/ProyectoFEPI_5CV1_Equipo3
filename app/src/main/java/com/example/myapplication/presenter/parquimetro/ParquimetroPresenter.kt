package com.example.myapplication.presenter.parquimetro

import com.example.myapplication.view.parquimetros.ParquimetroContract

class ParquimetroPresenter(private var view: ParquimetroContract.View?) : ParquimetroContract.Presenter {

    override fun alHacerClickConsultar() {
        // Aquí podrías validar si el usuario tiene permisos o si hay internet antes de navegar
        view?.navegarAStatus()
    }

    override fun alHacerClickConfiguracion() {
        view?.navegarAConfiguracion()
    }

    override fun alHacerClickReporte() {
        // --- CORRECCIÓN DE LÓGICA ---
        // Ahora llama a la función correcta para navegar a la pantalla de reporte/contacto
        view?.navegarAReporte()
    }

    override fun destruir() {
        view = null // Evitamos fugas de memoria (memory leaks)
    }
}
