package com.example.myapplication.view.operador_grua

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes
import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.presenter.operador_grua.SolicitudArrastrePresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Solicitud_ArrastreActivity : ComponentActivity(), SolicitudArrastreContract.View {

    private lateinit var presenter: SolicitudArrastreContract.Presenter
    private lateinit var rvSolicitudes: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var fabRefresh: FloatingActionButton
    private lateinit var adapter: SolicitudesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_nuevas)

        rvSolicitudes = findViewById(R.id.rvSolicitudes)
        layoutVacio = findViewById(R.id.layoutVacio)
        fabRefresh = findViewById(R.id.fabRefresh)

        setupRecyclerView()

        presenter = SolicitudArrastrePresenter(this, SolicitudArrastreModel())

        fabRefresh.setOnClickListener {
            presenter.cargarSolicitudesNuevas()
        }

        presenter.cargarSolicitudesNuevas()
    }

    private fun setupRecyclerView() {
        adapter = SolicitudesAdapter(emptyList()) { solicitud ->
            if (solicitud.infraccion_id != null) {
                val intent = Intent(this, DetalleSolicitudActivity::class.java)
                intent.putExtra("id_arrastre", solicitud.id)
                intent.putExtra("document_id", solicitud.documentId)  // ¡IMPORTANTE!
                intent.putExtra("id_infraccion", solicitud.infraccion_id.id)
                intent.putExtra("observaciones", solicitud.observaciones)
                intent.putExtra("Ubicacion", solicitud.ubicacion_arrastre)
                intent.putExtra("folio", solicitud.folio)  // Opcional
                startActivity(intent)
            } else {
                Toast.makeText(this, "Datos de la infracción incompletos. No se puede proceder.", Toast.LENGTH_LONG).show()
            }
        }
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter = adapter
    }

    override fun mostrarSolicitudes(lista: List<GenerarArrastreAttributes>) {
        android.util.Log.d("SOLICITUDES", "Recibidas ${lista.size} solicitudes")

        lista.forEachIndexed { index, solicitud ->
            android.util.Log.d("SOLICITUDES", "Solicitud $index:")
            android.util.Log.d("SOLICITUDES", "  Folio: ${solicitud.folio}")
            android.util.Log.d("SOLICITUDES", "  Ubicación: ${solicitud.ubicacion_arrastre}")
            android.util.Log.d("SOLICITUDES", "  Estatus: ${solicitud.estatus}")
            android.util.Log.d("SOLICITUDES", "  Tiene infracción: ${solicitud.infraccion_id != null}")
        }

        if (lista.isEmpty()) {
            android.util.Log.d("SOLICITUDES", "Lista vacía - mostrando layout vacío")
            rvSolicitudes.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            android.util.Log.d("SOLICITUDES", "Lista con datos - mostrando RecyclerView")
            rvSolicitudes.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            adapter.updateData(lista)
        }
    }

    override fun confirmarAceptacion(folio: String) {
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
        rvSolicitudes.visibility = View.GONE
        layoutVacio.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}