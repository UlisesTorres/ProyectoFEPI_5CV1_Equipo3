package com.example.myapplication.view.corralones

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.corralones.LiberarModel
import com.example.myapplication.presenter.corralones.LiberarPresenter

class LiberarActivity : ComponentActivity(), LiberarContract.View {

    private lateinit var presenter: LiberarContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liberar_auto)

        presenter = LiberarPresenter(this, LiberarModel())

        // Aquí asociarías un botón de búsqueda:
        // btnBuscar.setOnClickListener { presenter.buscarVehiculoParaLiberar(etBusqueda.text.toString()) }

        // Y un botón de confirmación:
        // btnConfirmar.setOnClickListener { presenter.procesarLiberacion(idVehiculo) }
    }

    override fun mostrarDatosVehiculo(detalles: String) {
        // Pintar los datos en los TextViews correspondientes
    }

    override fun confirmarSalidaExitosa() {
        // Toast.makeText(this, "Vehículo liberado correctamente", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun mostrarCargando() { /* ProgressBar ON */ }
    override fun ocultarCargando() { /* ProgressBar OFF */ }
    override fun mostrarError(mensaje: String) { /* Mostrar mensaje al usuario */ }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}