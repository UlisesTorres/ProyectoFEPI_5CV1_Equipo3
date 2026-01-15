package com.example.myapplication.view.transito

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.model.transito.InfraccionData
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

        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }

        presenter.obtenerInfraccionesDelOficial()
    }

    override fun mostrarListaInfracciones(infracciones: List<InfraccionData>) {
        if (infracciones.isEmpty()) {
            recyclerView.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            // DESACTIVADO EL CLIC: Al pasar un bloque vacío {}, el adaptador no hará nada al tocar los cuadros
            recyclerView.adapter = HistorialAdapter(infracciones) { 
                // No hace nada al seleccionar
            }
        }
    }

    override fun navegarADetalleInfraccion(infraccion: InfraccionData) {
        // Esta función se queda vacía porque ya no navegamos desde aquí
    }

    override fun mostrarCargando() {}
    override fun ocultarCargando() {}
    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
