package com.example.myapplication.view.transito

import HistorialAdapter
import android.content.Intent // Importamos Intent para la navegación
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.model.transito.InfraccionAttributes
import com.example.myapplication.presenter.transito.HistorialInfraccionesPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HistorialActivity : ComponentActivity(), HistorialInfraccionesContract.View {

    private lateinit var presenter: HistorialInfraccionesContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infracciones_registradas)

        recyclerView = findViewById(R.id.rvInfraccionesHistorial)
        layoutVacio = findViewById(R.id.layoutHistorialVacio)
        recyclerView.layoutManager = LinearLayoutManager(this)
        actualizarAdaptador(emptyList()) // Inicializa con lista vacía

        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }

        presenter.obtenerInfraccionesDelOficial()
    }

    override fun mostrarListaInfracciones(infracciones: List<InfraccionAttributes>) {
        if (infracciones.isEmpty()) {
            recyclerView.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            actualizarAdaptador(infracciones)
        }
    }

    private fun actualizarAdaptador(infracciones: List<InfraccionAttributes>) {
        val adapter = HistorialAdapter(infracciones) { infraccion ->
            // --- CORRECCIÓN #1: Pasa el objeto 'infraccion' completo ---
            presenter.alSeleccionarInfraccion(infraccion)
        }
        recyclerView.adapter = adapter
    }

    override fun mostrarCargando() {
        // Implementar
    }

    override fun ocultarCargando() {
        // Implementar
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        recyclerView.visibility = View.GONE
        layoutVacio.visibility = View.VISIBLE
    }

    override fun navegarADetalleInfraccion(infraccion: InfraccionAttributes) {
        // Implementamos la navegación que habíamos planeado
        val intent = Intent(this, DetalleInfraccionActivity::class.java).apply {
            putExtra("EXTRA_FOLIO", infraccion.folio)
            putExtra("EXTRA_PLACA", infraccion.placa)
            putExtra("EXTRA_FECHA", infraccion.fecha)
            // Puedes añadir más datos aquí si los necesitas
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
