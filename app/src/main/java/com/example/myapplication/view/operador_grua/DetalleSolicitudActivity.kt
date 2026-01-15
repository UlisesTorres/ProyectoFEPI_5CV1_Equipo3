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
    private var idArrastre: Int = -1
    private lateinit var documentId: String
    private var idInfraccion: Int = -1

    // ¡IMPORTANTE! Inicializa los TextView de manera segura
    private lateinit var tvIdInfraccion: TextView
    private lateinit var tvObservaciones: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var btnAceptar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_det_sol_nva)

        // **PRIMERO** inicializa todas las views
        inicializarViews()

        presenter = DetalleSolicitudPresenter(this, SolicitudArrastreModel())

        // Obtener todos los datos necesarios
        idArrastre = intent.getIntExtra("id_arrastre", -1)
        documentId = intent.getStringExtra("document_id") ?: ""
        idInfraccion = intent.getIntExtra("id_infraccion", -1)
        val observaciones = intent.getStringExtra("observaciones") ?: "Sin observaciones"
        val ubicacion = intent.getStringExtra("Ubicacion") ?: "Ubicación no disponible"

        // **DESPUÉS** de inicializar, usa las views
        mostrarDatosEnUI(idInfraccion, observaciones, ubicacion)

        configurarBotonAceptar()
    }

    private fun inicializarViews() {
        // Aquí inicializamos TODAS las views
        tvIdInfraccion = findViewById(R.id.tv_detalle_infraccion_id)
        tvObservaciones = findViewById(R.id.tv_detalle_observaciones)
        tvUbicacion = findViewById(R.id.tv_detalle_ubicacion)
        btnAceptar = findViewById(R.id.btn_aceptar_solicitud)
    }

    private fun mostrarDatosEnUI(idInfraccion: Int, observaciones: String, ubicacion: String) {
        // Ahora sí podemos usar las views porque ya están inicializadas
        tvIdInfraccion.text = "ID Infracción: $idInfraccion"
        tvObservaciones.text = "Observaciones: $observaciones"
        tvUbicacion.text = "Ubicación: $ubicacion"
    }

    private fun configurarBotonAceptar() {
        btnAceptar.setOnClickListener {
            val prefs = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
            val nombreUsuario = prefs.getString("username", "Usuario Desconocido") ?: "Usuario Desconocido"

            if (idArrastre != -1 && documentId.isNotEmpty() && idInfraccion != -1) {
                presenter.aceptarSolicitud(
                    idArrastre = idArrastre,
                    documentId = documentId,
                    idInfraccion = idInfraccion,
                    operadorGrua = nombreUsuario,
                    gruaIdentificador = nombreUsuario
                )
            } else {
                mostrarError("Datos incompletos")
                android.util.Log.e("DETALLE", "Datos incompletos: idArrastre=$idArrastre, documentId=$documentId, idInfraccion=$idInfraccion")
            }
        }
    }

    override fun mostrarCargando() {
        // Muestra ProgressBar si tienes
        // findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
    }

    override fun ocultarCargando() {
        // Oculta ProgressBar si tienes
        // findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
    }

    override fun mostrarConfirmacion(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        // Cerrar automáticamente después de 2 segundos
        android.os.Handler(mainLooper).postDelayed({
            cerrarVista()
        }, 2000)
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