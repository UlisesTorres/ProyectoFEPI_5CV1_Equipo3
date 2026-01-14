package com.example.myapplication.presenter.transito

import com.example.myapplication.model.parquimetros.EstatusModel
import com.example.myapplication.model.parquimetros.TipoResultado
import com.example.myapplication.view.transito.EstatusContract

class EstatusPresenter(
    private var view: EstatusContract.View?,
    private val model: EstatusModel
) : EstatusContract.Presenter {

    override fun consultarPlaca(placa: String) {
        if (placa.isBlank()) {
            view?.mostrarError("Por favor, ingrese una placa.")
            return
        }
        view?.mostrarCargando()

        model.buscarPlacaEnServidor(placa) { resultado ->
            view?.ocultarCargando()
            when (resultado.tipo) {
                TipoResultado.VIGENTE -> {
                    // Este es el único caso que debe llamar a mostrarEstatus()
                    view?.mostrarEstatus(resultado.mensaje, true)
                }
                // --- INICIO DE LA CORRECCIÓN ---
                // Agrupamos todos los casos "no válidos" para que llamen a mostrarError()
                TipoResultado.CADUCADO,
                TipoResultado.NO_ENCONTRADO,
                TipoResultado.ERROR_SERVIDOR,
                TipoResultado.ERROR_RED -> {
                    view?.mostrarError(resultado.mensaje)
                }
                // --- FIN DE LA CORRECCIÓN ---
            }
        }
    }

    override fun destruir() {
        view = null
    }
}