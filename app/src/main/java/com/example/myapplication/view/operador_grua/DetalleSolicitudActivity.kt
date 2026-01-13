package com.example.myapplication.view.operador_grua

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.SolicitudArrastreModel
import com.example.myapplication.presenter.operador_grua.DetalleSolicitudPresenter

class DetalleSolicitudActivity : ComponentActivity(), DetalleSolicitudContract.View {

    private lateinit var presenter: DetalleSolicitudContract.Presenter
    private var idInfraccion: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_solicitud)

        presenter = DetalleSolicitudPresenter(this, SolicitudArrastreModel())

        // --- CORRECCIÓN: Leemos el ID de la infracción ---
        idInfraccion = intent.getIntExtra("id_infraccion", -1)
        val observaciones = intent.getStringExtra("observaciones")

        val tvIdInfraccion: TextView = findViewById(R.id.tv_detalle_folio)
        val tvObservaciones: TextView = findViewById(R.id.tv_detalle_placa)

        // --- CORRECCIÓN: Mostramos el ID de la infracción ---
        tvIdInfraccion.text = "ID Infracción: $idInfraccion"
        tvObservaciones.text = "Observaciones: $observaciones"

        val btnAceptar: Button = findViewById(R.id.btn_aceptar_solicitud)
        btnAceptar.setOnClickListener {
            // --- CORRECCIÓN: Pasamos el ID de la infracción al presentador ---
            if (idInfraccion != -1) {
                presenter.aceptarSolicitud(idInfraccion)
            } else {
                mostrarError("ID de infracción no válido")
            }
        }
    }

    override fun mostrarCargando() {
        // Aquí podrías mostrar un ProgressBar
    }

    override fun ocultarCargando() {
        // Aquí ocultarías el ProgressBar
    }

    override fun mostrarConfirmacion(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun cerrarVista() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
