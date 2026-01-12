package com.example.myapplication.view.transito

import HistorialAdapter
import android.content.Intent // Podrías necesitar esto para la navegación
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
    // private lateinit var adapter: HistorialAdapter // No necesitas que sea una propiedad de la clase
    private lateinit var recyclerView: RecyclerView // Haz que el RecyclerView sea una propiedad
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infracciones_registradas)

        // Inicializa las vistas
        recyclerView = findViewById(R.id.rvInfraccionesHistorial)
        layoutVacio = findViewById(R.id.layoutHistorialVacio)

        // Configura el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el adaptador con una lista vacía y una acción de clic
        actualizarAdaptador(emptyList())

        // Configura el Presenter
        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }

        // Carga inicial
        presenter.obtenerInfraccionesDelOficial()
    }

    override fun mostrarListaInfracciones(infracciones: List<InfraccionAttributes>) {
        if (infracciones.isEmpty()) {
            recyclerView.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            // CORRECCIÓN #2: Llama a la nueva función para actualizar el adaptador
            actualizarAdaptador(infracciones)
        }
    }

    // CORRECCIÓN #1: Crea una función para manejar la creación y actualización del adaptador
    private fun actualizarAdaptador(infracciones: List<InfraccionAttributes>) {
        val adapter = HistorialAdapter(infracciones) { infraccion ->
            // Aquí manejas lo que sucede cuando se hace clic en una tarjeta
            // Llama al presenter para manejar la lógica de negocio
            presenter.alSeleccionarInfraccion(infraccion.folio)
        }
        recyclerView.adapter = adapter
    }

    override fun mostrarCargando() {
        // Implementar si se agrega un ProgressBar al XML
    }

    override fun ocultarCargando() {
        // Implementar si se agrega un ProgressBar al XML
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        // Oculta la lista y muestra el layout vacío si hay un error
        recyclerView.visibility = View.GONE
        layoutVacio.visibility = View.VISIBLE
    }

    override fun navegarADetalleInfraccion(folio: String) {
        // Ejemplo de cómo podrías navegar
        Toast.makeText(this, "Navegando al detalle del folio: $folio", Toast.LENGTH_SHORT).show()
        // val intent = Intent(this, DetalleInfraccionActivity::class.java)
        // intent.putExtra("FOLIO_INFRACCION", folio)
        // startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
