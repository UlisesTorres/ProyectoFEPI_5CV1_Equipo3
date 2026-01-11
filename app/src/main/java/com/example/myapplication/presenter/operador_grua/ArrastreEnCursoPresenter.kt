package com.example.myapplication.presenter.operador_grua

import com.example.myapplication.model.operador_grua.ArrastreEnCursoModel
import com.example.myapplication.view.operador_grua.ArrastreEnCursoContract

class ArrastreEnCursoPresenter(
    private var view: ArrastreEnCursoContract.View?,
    private val model: ArrastreEnCursoModel
) : ArrastreEnCursoContract.Presenter {

    override fun obtenerServicioActivo() {
        view?.mostrarCargando()
        model.consultarServicioActivo { datos, exito ->
            view?.ocultarCargando()
            if (exito && datos != null) {
                view?.mostrarDetallesArrastre(
                    datos["folio"]!!,
                    datos["placa"]!!,
                    datos["destino"]!!
                )
            } else {
                view?.mostrarError("No se encontró un arrastre activo")
            }
        }
    }

    override fun terminarArrastre(idServicio: String) {
        // Lógica para enviar a la API que el vehículo ya llegó al corralón
        view?.finalizarServicioExitoso()
    }

    override fun destruir() {
        view = null
    }
}