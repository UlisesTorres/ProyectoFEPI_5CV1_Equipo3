package com.example.myapplication.view.operador_grua

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.presenter.operador_grua.SolicitudArrastrePresenter

class Solicitud_ArrastreActivity : ComponentActivity(), SolicitudArrastreContract.View {

    private lateinit var presenter: SolicitudArrastreContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_nuevas)

        presenter = SolicitudArrastrePresenter(this, SolicitudArrastreModel())

        // Cargar solicitudes al abrir
        presenter.cargarSolicitudesNuevas()
    }

    override fun mostrarSolicitudes(lista: List<String>) {
        // Aquí llenas tu lista (RecyclerView)
        // Al hacer clic en un elemento de la lista, llamarías a:
        // presenter.aceptarSolicitud(idSeleccionado)
    }

    override fun confirmarAceptacion(folio: String) {
        // Avisar al usuario y quizás mandarlo a la pantalla de "Arrastre en curso"
        // Toast.makeText(this, "Vas en camino al folio $folio", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun mostrarCargando() { /* ProgressBar.VISIBLE */ }
    override fun ocultarCargando() { /* ProgressBar.GONE */ }
    override fun mostrarError(mensaje: String) { /* Toast o TextView vacío */ }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}