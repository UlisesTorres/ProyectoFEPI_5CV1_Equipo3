package com.example.myapplication.view.corralones

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.corralones.HistorialCorralonModel
import com.example.myapplication.presenter.corralones.HistorialCorralonPresenter

class HistorialCorralonActivity : ComponentActivity(), HistorialCorralonContract.View {

    private lateinit var presenter: HistorialCorralonContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_corralon)

        presenter = HistorialCorralonPresenter(this, HistorialCorralonModel())

        // Cargar los registros al iniciar
        presenter.obtenerHistorialCompleto()
    }

    override fun mostrarMovimientos(movimientos: List<String>) {
        // Vincular con el RecyclerView.Adapter
        // adapter.setItems(movimientos)
    }

    override fun mostrarCargando() {
        // Mostrar ProgressBar circular
    }

    override fun ocultarCargando() {
        // Ocultar ProgressBar
    }

    override fun mostrarError(mensaje: String) {
        // Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}