package com.example.myapplication.view.transito

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.model.parquimetros.EstatusModel
import com.example.myapplication.presenter.transito.EstatusPresenter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class EstatusActivity : ComponentActivity(), EstatusContract.View {

    private lateinit var presenter: EstatusContract.Presenter

    // --- Declaración de Vistas con los IDs de tu XML ---
    private lateinit var etPlaca: TextInputEditText
    private lateinit var btnConsultar: Button
    private lateinit var cardResultado: MaterialCardView
    private lateinit var tvStatusResultado: TextView
    private lateinit var tvTiempoRestante: TextView // Para mostrar el mensaje completo
    private lateinit var pbConsulta: ProgressBar // Asumiendo que tienes un ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatus)

        // --- Inicialización de Vistas con tus IDs ---
        etPlaca = findViewById(R.id.etPlacaBusqueda)
        btnConsultar = findViewById(R.id.btnBuscarPlaca)
        cardResultado = findViewById(R.id.cardResultado)
        tvStatusResultado = findViewById(R.id.tvStatusResultado)
        tvTiempoRestante = findViewById(R.id.tvTiempoRestante)

        // Asume que tienes un ProgressBar en tu layout. Si no, puedes ignorar esta línea.
        // Por ejemplo, podrías añadir un ProgressBar centrado en el ConstraintLayout.
        // pbConsulta = findViewById(R.id.pb_parquimetro)


        // Inyectamos el modelo al presenter
        presenter = EstatusPresenter(this, EstatusModel())

        // Configuración del listener del botón
        btnConsultar.setOnClickListener {
            val placa = etPlaca.text.toString().trim().uppercase() // Convertimos a mayúsculas
            presenter.consultarPlaca(placa)
        }
    }

    override fun mostrarCargando() {
        // Ocultamos la tarjeta de resultado y el botón mientras se carga
        cardResultado.visibility = View.GONE
        btnConsultar.isEnabled = false
        // pbConsulta.visibility = View.VISIBLE // Muestra el ProgressBar
    }

    override fun ocultarCargando() {
        // pbConsulta.visibility = View.GONE // Oculta el ProgressBar
        btnConsultar.isEnabled = true
    }

    override fun mostrarEstatus(mensaje: String, esValido: Boolean) {
        cardResultado.visibility = View.VISIBLE

        // El 'mensaje' que viene del modelo ya es bastante completo.
        // Lo dividimos para ponerlo en los TextViews correspondientes.
        val partesMensaje = mensaje.split("\n")

        if (esValido) {
            tvStatusResultado.text = "VIGENTE"
            tvStatusResultado.setTextColor(ContextCompat.getColor(this, R.color.verde_vigente)) // Usa un color de colors.xml
            tvTiempoRestante.text = partesMensaje.getOrElse(2) { "" } // Muestra la zona
        }

        // Ocultamos el textview de la hora de vencimiento ya que no tenemos ese dato por ahora
        findViewById<TextView>(R.id.tvHoraVencimiento).visibility = View.GONE
    }

    override fun mostrarError(error: String) {
        cardResultado.visibility = View.VISIBLE

        val partesError = error.split("\n")
        val status = if (partesError[0].contains("EXPIRADO")) "EXPIRADO" else "NO ENCONTRADO"

        tvStatusResultado.text = status
        tvStatusResultado.setTextColor(ContextCompat.getColor(this, R.color.rojo_error)) // Usa un color de colors.xml
        tvTiempoRestante.text = partesError.getOrElse(0) { error } // Muestra el mensaje de error completo

        findViewById<TextView>(R.id.tvHoraVencimiento).visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
