package com.example.myapplication.view.transito

import HistorialAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.model.transito.InfraccionAttributes
import com.example.myapplication.presenter.transito.SeleccionarInfraccionPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException

class SeleccionarInfraccionActivity : AppCompatActivity(), SeleccionarInfraccionContract.View {

    // 1. Declaramos las variables para las vistas
    private lateinit var rvInfracciones: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var fabRefresh: FloatingActionButton

    private lateinit var presenter: SeleccionarInfraccionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Esta es la línea que querías mantener, y es correcta para este enfoque
        setContentView(R.layout.activity_seleccionar_infraccion)

        // 2. Inicializamos las vistas usando findViewById
        rvInfracciones = findViewById(R.id.rvInfraccionesHistorial)
        layoutVacio = findViewById(R.id.layoutHistorialVacio)
        fabRefresh = findViewById(R.id.fabRefresh)

        presenter = SeleccionarInfraccionPresenter(this, HistorialInfraccionesModel())

        setupRecyclerView()

        fabRefresh.setOnClickListener {
            presenter.obtenerInfraccionesRecientes()
        }

        presenter.obtenerInfraccionesRecientes()
    }

    private fun setupRecyclerView() {
        rvInfracciones.layoutManager = LinearLayoutManager(this)
    }

    override fun mostrarInfraccionesRecientes(infracciones: List<InfraccionAttributes>) {
        val infraccionesFiltradas = infracciones.filter { esReciente(it.fecha) }

        if (infraccionesFiltradas.isEmpty()) {
            rvInfracciones.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            rvInfracciones.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            actualizarAdaptador(infraccionesFiltradas)
        }
    }

    private fun esReciente(fechaInfraccionStr: String): Boolean {
        return try {
            val fechaInfraccion = Instant.parse(fechaInfraccionStr)
            val ahora = Instant.now()
            val diferencia = Duration.between(fechaInfraccion, ahora)
            diferencia.toHours() < 1
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun actualizarAdaptador(infracciones: List<InfraccionAttributes>) {
        val adapter = HistorialAdapter(infracciones) { infraccion ->
            presenter.seleccionarInfraccion(infraccion)
        }
        rvInfracciones.adapter = adapter
    }

    // CORRECCIÓN LÓGICA: Añadimos el ID de la infracción al Intent
    override fun navegarAOrdenArrastre(infraccion: InfraccionAttributes) {
        val intent = Intent(this, Orden_ArrastreActivity::class.java).apply {
            // Este es el dato más importante para que la siguiente pantalla funcione
            putExtra("EXTRA_ID_INFRACCION", infraccion.id)
            putExtra("EXTRA_FOLIO", infraccion.folio)
            putExtra("EXTRA_PLACA", infraccion.placa)
        }
        startActivity(intent)
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
