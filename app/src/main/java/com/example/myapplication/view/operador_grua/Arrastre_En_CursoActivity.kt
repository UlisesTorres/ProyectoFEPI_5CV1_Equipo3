package com.example.myapplication.view.operador_grua

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.ArrastreEnCursoModel
import com.example.myapplication.presenter.operador_grua.ArrastreEnCursoPresenter

class Arrastre_En_CursoActivity : ComponentActivity(), ArrastreEnCursoContract.View {

    private lateinit var presenter: ArrastreEnCursoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrastre_en_curso)

        presenter = ArrastreEnCursoPresenter(this, ArrastreEnCursoModel())

        // Al iniciar, pedimos los datos del servicio actual
        presenter.obtenerServicioActivo()
    }

    override fun mostrarDetallesArrastre(folio: String, placa: String, destino: String) {
        // Aquí asignarías los valores a tus TextViews del XML
        // tvFolio.text = folio
        // tvPlaca.text = placa
        // tvDestino.text = destino
    }

    override fun mostrarCargando() { /* Mostrar ProgressBar */ }

    override fun ocultarCargando() { /* Ocultar ProgressBar */ }

    override fun mostrarError(mensaje: String) {
        // Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun finalizarServicioExitoso() {
        finish() // Regresa a la pantalla principal del operador
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}

