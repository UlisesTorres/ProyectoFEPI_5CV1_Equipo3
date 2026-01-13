package com.example.myapplication.view.operador_grua

import android.content.Context
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
        setContentView(R.layout.activity_det_sol_nva)

        presenter = DetalleSolicitudPresenter(this, SolicitudArrastreModel())

        idInfraccion = intent.getIntExtra("id_infraccion", -1)
        val observaciones = intent.getStringExtra("observaciones")
        val ubicacion = intent.getStringExtra("Ubicacion")


        val tvIdInfraccion: TextView = findViewById(R.id.tv_detalle_infraccion_id)
        val tvObservaciones: TextView = findViewById(R.id.tv_detalle_observaciones)
        val tvUbicacion: TextView = findViewById(R.id.tv_detalle_ubicacion)


        tvIdInfraccion.text = "ID Infracción: $idInfraccion"
        tvObservaciones.text = "Observaciones: $observaciones"
        tvUbicacion.text = "Ubicación: $ubicacion"



        val btnAceptar: Button = findViewById(R.id.btn_aceptar_solicitud)
        btnAceptar.setOnClickListener {
            // --- CORRECCIÓN FINAL: Usamos las claves correctas de SharedPreferences ---
            val prefs = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
            val nombreUsuario = prefs.getString("username", "Usuario Desconocido") ?: "Usuario Desconocido"

            if (idInfraccion != -1) {
                // Usamos el nombre de usuario para ambos campos, como se solicitó.
                presenter.aceptarSolicitud(idInfraccion, nombreUsuario, nombreUsuario)
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
