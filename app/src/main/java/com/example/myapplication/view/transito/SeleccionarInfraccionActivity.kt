package com.example.myapplication.view.transito

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
import com.example.myapplication.model.transito.InfraccionData
import com.example.myapplication.presenter.transito.SeleccionarInfraccionPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException

class SeleccionarInfraccionActivity : AppCompatActivity(), SeleccionarInfraccionContract.View {

    private lateinit var rvInfracciones: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var fabRefresh: FloatingActionButton
    private lateinit var presenter: SeleccionarInfraccionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_infraccion)

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

    override fun mostrarInfraccionesRecientes(infracciones: List<InfraccionData>) {
        val infraccionesFiltradas = infracciones.filter { 
            val fecha = it.fecha_infraccion ?: ""
            esReciente(fecha) 
        }

        if (infraccionesFiltradas.isEmpty()) {
            rvInfracciones.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            rvInfracciones.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            rvInfracciones.adapter = HistorialAdapter(infraccionesFiltradas) { infraccion ->
                presenter.seleccionarInfraccion(infraccion)
            }
        }
    }

    private fun esReciente(fechaInfraccionStr: String): Boolean {
        if (fechaInfraccionStr.isEmpty()) return false
        return try {
            val fechaInfraccion = Instant.parse(fechaInfraccionStr)
            val ahora = Instant.now()
            val diferencia = Duration.between(fechaInfraccion, ahora)
            diferencia.toHours() < 1
        } catch (e: DateTimeParseException) {
            false
        }
    }

    override fun navegarAOrdenArrastre(infraccion: InfraccionData) {
        val intent = Intent(this, Orden_ArrastreActivity::class.java).apply {
            putExtra("EXTRA_ID_INFRACCION", infraccion.id ?: -1)
            putExtra("EXTRA_FOLIO", infraccion.folio ?: "S/F")
            putExtra("EXTRA_PLACA", infraccion.placa_vehiculo ?: "S/P")
            putExtra("EXTRA_UBICACION", infraccion.ubicacion_infraccion ?: "N/D")
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
