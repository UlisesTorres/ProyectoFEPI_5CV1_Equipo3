package com.example.myapplication.view.operador_grua

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
// --- CORRECCIÓN #1: Cambiamos la importación ---
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes
import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.presenter.operador_grua.SolicitudArrastrePresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Solicitud_ArrastreActivity : ComponentActivity(), SolicitudArrastreContract.View {

    private lateinit var presenter: SolicitudArrastreContract.Presenter

    // --- Declaramos las vistas del layout ---
    private lateinit var rvSolicitudes: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var fabRefresh: FloatingActionButton
    private lateinit var adapter: SolicitudesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_nuevas)

        // --- Inicializamos las vistas ---
        rvSolicitudes = findViewById(R.id.rvSolicitudes)
        layoutVacio = findViewById(R.id.layoutVacio)
        fabRefresh = findViewById(R.id.fabRefresh)

        // --- Configuramos el RecyclerView y el Adaptador ---
        setupRecyclerView()

        presenter = SolicitudArrastrePresenter(this, SolicitudArrastreModel())

        fabRefresh.setOnClickListener {
            presenter.cargarSolicitudesNuevas()
        }

        // Cargar solicitudes al abrir la pantalla
        presenter.cargarSolicitudesNuevas()
    }

    private fun setupRecyclerView() {
        // Creamos el adaptador con una lista vacía y la acción de clic
        // El tipo de dato aquí ya coincide con el adaptador que corregimos antes
        adapter = SolicitudesAdapter(emptyList()) { solicitudSeleccionada ->
            presenter.aceptarSolicitud(solicitudSeleccionada)
        }
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter = adapter
    }

    // --- CORRECCIÓN #2: Actualizamos el tipo de la lista ---
    override fun mostrarSolicitudes(lista: List<GenerarArrastreAttributes>) {
        if (lista.isEmpty()) {
            // Si la lista está vacía, mostramos el layout vacío
            rvSolicitudes.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            // Si hay datos, mostramos el RecyclerView y actualizamos el adaptador
            rvSolicitudes.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            // La 'lista' ahora es del tipo correcto y compatible con el método updateData del adaptador
            adapter.updateData(lista)
        }
    }

    override fun confirmarAceptacion(folio: String) {
        // Esto se usará cuando implementes la lógica de aceptar
        Toast.makeText(this, "Aceptando solicitud para folio $folio...", Toast.LENGTH_SHORT).show()
    }

    override fun mostrarCargando() {
        fabRefresh.isEnabled = false
    }

    override fun ocultarCargando() {
        fabRefresh.isEnabled = true
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        // Si hay un error, también mostramos la vista vacía
        rvSolicitudes.visibility = View.GONE
        layoutVacio.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
