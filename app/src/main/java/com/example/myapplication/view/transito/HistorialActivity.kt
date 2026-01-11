package com.example.myapplication.view.transito

import android.os.Bundle
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

    // 1. Declaramos el RecyclerView y el Adapter
    private lateinit var recyclerView: RecyclerView
    // Nota: Mañana crearemos la clase HistorialAdapter
    // private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infracciones_registradas)

        // Vinculamos el ID correcto del XML corregido
        val recyclerView = findViewById<RecyclerView>(R.id.rvInfraccionesHistorial)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // MAÑANA: Crearemos el adapter y lo pondremos aquí:
        // val adapter = MiAdapter(mutableListOf())
        // recyclerView.adapter = adapter

        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        // El FAB de refresh que agregaste en el XML
        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }
    }

    override fun mostrarListaInfracciones(infracciones: List<String>) {
        // 4. Cuando lleguen los datos, actualizamos el adapter
        // adapter.updateData(infracciones)
    }

    override fun mostrarCargando() {
        // Opcional: Mostrar un ProgressBar
    }

    override fun ocultarCargando() {
        // Ocultar ProgressBar
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarADetalleInfraccion(folio: String) {
        // Navegación al detalle si es necesario
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}