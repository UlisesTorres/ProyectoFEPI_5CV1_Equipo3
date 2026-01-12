package com.example.myapplication.view.supervisor

// Imports necesarios que copiaremos de HistorialActivity
import HistorialAdapter
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
import com.example.myapplication.model.transito.InfraccionAttributes
import com.example.myapplication.presenter.transito.HistorialInfraccionesPresenter
import com.example.myapplication.view.transito.DetalleInfraccionActivity // Necesario para navegar al detalle
import com.example.myapplication.view.transito.HistorialInfraccionesContract // Reutilizamos el mismo contrato
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Esta clase es una adaptación de HistorialActivity para el rol de Supervisor.
 * Reutiliza el mismo Presenter, Model y Contract, asumiendo que el supervisor
 * ve la misma lista de infracciones o que la API devuelve la lista apropiada
 * para el rol de supervisor.
 */
class SupervisorHistorialActivity : ComponentActivity(), HistorialInfraccionesContract.View {

    // Reutilizamos el mismo Presenter y componentes de la vista
    private lateinit var presenter: HistorialInfraccionesContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_supervisor_historial)

        recyclerView = findViewById(R.id.rvInfraccionesHistorial)
        layoutVacio = findViewById(R.id.layoutHistorialVacio)
        recyclerView.layoutManager = LinearLayoutManager(this)
        actualizarAdaptador(emptyList())

        // Reutilizamos el mismo Model y Presenter. ¡Esto es eficiencia!
        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }

        // Carga inicial de datos
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
        // Reutilizamos el mismo HistorialAdapter
        val adapter = HistorialAdapter(infracciones) { infraccion ->
            presenter.alSeleccionarInfraccion(infraccion)
        }
        recyclerView.adapter = adapter
    }

    override fun mostrarCargando() {
        // Implementar si es necesario (ej. mostrar un ProgressBar)
    }

    override fun ocultarCargando() {
        // Implementar si es necesario (ej. ocultar un ProgressBar)
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        recyclerView.visibility = View.GONE
        layoutVacio.visibility = View.VISIBLE
    }

    // La navegación al detalle es idéntica
    override fun navegarADetalleInfraccion(infraccion: InfraccionAttributes) {
        val intent = Intent(this, DetalleInfraccionActivity::class.java).apply {
            putExtra("EXTRA_FOLIO", infraccion.folio)
            putExtra("EXTRA_PLACA", infraccion.placa)
            putExtra("EXTRA_FECHA", infraccion.fecha)
            // Si en el futuro necesitas la URL de la evidencia, también la pasarías aquí
            // putExtra("EXTRA_URL_EVIDENCIA", infraccion.urlEvidencia)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
