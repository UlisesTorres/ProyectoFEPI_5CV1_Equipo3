package com.example.myapplication.view.operador_grua

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.HistorialArrastresModel
import com.example.myapplication.presenter.operador_grua.HistorialArrastresPresenter

class Historial_ArrastresActivity : ComponentActivity(), HistorialArrastresContract.View {

    private lateinit var presenter: HistorialArrastresContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_arrastres)

        presenter = HistorialArrastresPresenter(this, HistorialArrastresModel())

        // Pedimos al presenter que inicie la carga de datos
        presenter.cargarHistorial()
    }

    override fun mostrarHistorial(lista: List<String>) {
        // Aquí conectarías tu Adapter del RecyclerView
        // adapter.updateData(lista)
    }

    override fun mostrarCargando() { /* Mostrar spinner */ }

    override fun ocultarCargando() { /* Ocultar spinner */ }

    override fun mostrarError(mensaje: String) {
        // Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarADetalle(idArrastre: String) {
        // Intent para ver el detalle de un arrastre viejo
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}