package com.example.myapplication.view.transito

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.presenter.transito.HistorialInfraccionesPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HistorialActivity : ComponentActivity(), HistorialInfraccionesContract.View {

    private lateinit var presenter: HistorialInfraccionesContract.Presenter
    private lateinit var adapter: HistorialAdapter
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infracciones_registradas)

        val recyclerView = findViewById<RecyclerView>(R.id.rvInfraccionesHistorial)
        layoutVacio = findViewById(R.id.layoutHistorialVacio)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistorialAdapter(mutableListOf())
        recyclerView.adapter = adapter

        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }

        // Carga inicial
        presenter.obtenerInfraccionesDelOficial()
    }

    override fun mostrarListaInfracciones(infracciones: List<String>) {
        if (infracciones.isEmpty()) {
            layoutVacio.visibility = View.VISIBLE
        } else {
            layoutVacio.visibility = View.GONE
            adapter.updateData(infracciones)
        }
    }

    override fun mostrarCargando() {
        // Implementar si se agrega un ProgressBar al XML
    }

    override fun ocultarCargando() {
        // Implementar si se agrega un ProgressBar al XML
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarADetalleInfraccion(folio: String) {
        // Implementar navegación si es necesario
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
