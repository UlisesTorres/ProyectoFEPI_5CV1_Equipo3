package com.example.myapplication.view.transito

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.transito.OrdenArrastreData
import com.example.myapplication.model.transito.OrdenArrastreRequest
import com.example.myapplication.network.RetrofitSecureClient
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Orden_ArrastreActivity : ComponentActivity() {

    // Variables para guardar los datos recibidos de la infracción
    private var infraccionId: Int = -1
    private lateinit var folioInfraccion: String // Renombrado para claridad
    private lateinit var placa: String
    private lateinit var ubicacion: String


    // Declaración de las Vistas (Views) del layout
    private lateinit var tvFolioActual: TextView
    private lateinit var tvPlacaActual: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var etNotasPolicia: TextInputEditText
    private lateinit var btnSolicitarGrua: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orden_arrastre)

        recibirDatosDeInfraccion()
        inicializarVistas()
        mostrarDatosEnUI()

        btnSolicitarGrua.setOnClickListener {
            if (infraccionId != -1) {
                enviarOrdenDeArrastre()
            } else {
                Toast.makeText(this, "Error: ID de infracción no encontrado.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun recibirDatosDeInfraccion() {
        infraccionId = intent.getIntExtra("EXTRA_ID_INFRACCION", -1)
        folioInfraccion = intent.getStringExtra("EXTRA_FOLIO") ?: "No disponible"
        placa = intent.getStringExtra("EXTRA_PLACA") ?: "No disponible"
        ubicacion = intent.getStringExtra("EXTRA_UBICACION") ?: "No disponible"

    }

    private fun inicializarVistas() {
        tvFolioActual = findViewById(R.id.tvFolioActual)
        tvPlacaActual = findViewById(R.id.tvPlacaActual)
        etNotasPolicia = findViewById(R.id.etNotasPolicia)
        btnSolicitarGrua = findViewById(R.id.btnSolicitarGrua)
        tvUbicacion = findViewById(R.id.tv_detalle_ubicacion)
    }

    private fun mostrarDatosEnUI() {
        tvFolioActual.text = "Folio Infracción: $folioInfraccion"
        tvPlacaActual.text = "Placas: $placa"
        tvUbicacion.text = "Ubicacion: $ubicacion"
    }

    private fun enviarOrdenDeArrastre() {
        btnSolicitarGrua.isEnabled = false
        val observaciones = etNotasPolicia.text.toString().trim()

        // --- INICIO DE LA LÓGICA ACTUALIZADA ---

        // 1. Generar un folio único para el nuevo registro de arrastre.
        val folioArrastre = "ARR-${System.currentTimeMillis()}"

        // 2. Crear el cuerpo de la petición con los campos que la API espera.
        val request = OrdenArrastreRequest(
            data = OrdenArrastreData(
                folio = folioArrastre,             // Campo 'folio' para la tabla 'Generar_arrastre'
                infraccionId = this.infraccionId,  // La relación con la infracción
                observaciones = observaciones,      // Las notas del policía
                ubicacion = ubicacion
            )
        )

        // --- FIN DE LA LÓGICA ACTUALIZADA ---

        val call = RetrofitSecureClient.infraccionApiService.crearOrdenArrastre(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Orden_ArrastreActivity, "Orden de arrastre generada con éxito.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@Orden_ArrastreActivity, "Error al generar la orden: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    btnSolicitarGrua.isEnabled = true
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Orden_ArrastreActivity, "Fallo de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                btnSolicitarGrua.isEnabled = true
            }
        })
    }
}
