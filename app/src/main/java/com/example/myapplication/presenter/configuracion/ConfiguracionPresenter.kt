package com.example.myapplication.presenter.configuracion

import com.example.myapplication.model.configuracion.ConfiguracionModel
import com.example.myapplication.view.configuracion.ConfiguracionContract

class ConfiguracionPresenter(
    private var view: ConfiguracionContract.View?,
    private val model: ConfiguracionModel
) : ConfiguracionContract.Presenter {

    override fun cargarDatosUsuario() {
        val (nombre, correo, rol) = model.obtenerDatosUsuario()
        view?.mostrarUsuario(nombre, correo, rol)
    }

    override fun verificarServidor() {
        model.verificarServidor { conectado ->
            view?.mostrarEstadoServidor(conectado)
        }
    }

    override fun sincronizar() {
        // Por ahora simulamos sincronización
        view?.mostrarMensaje("Datos sincronizados correctamente")
    }

    override fun clickCerrar() {
        view?.mostrarConfirmacionCierre()
    }

    override fun confirmarCierreSesion() {
        model.limpiarDatosSesion { exito ->
            if (exito) {
                view?.mostrarMensaje("Sesión cerrada")
                view?.navegarAlLogin()
            }
        }
    }

    fun destruir() {
        view = null
    }
}
