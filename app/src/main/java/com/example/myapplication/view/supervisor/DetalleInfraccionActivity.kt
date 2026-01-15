package com.example.myapplication.view.supervisor

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.example.myapplication.R
import com.example.myapplication.network.RetrofitSecureClient
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleInfraccionActivity : AppCompatActivity() {

    private var infraccionId: Int = -1
    private lateinit var documentId: String
    private val BASE_URL_IMAGES = "https://apisistemainfracciones-production.up.railway.app"

    // Declara las views como variables de clase
    private lateinit var layoutBotonesValidacion: ConstraintLayout
    private lateinit var tvEstatusValidacion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_infraccion)

        // Inicializar las views primero
        layoutBotonesValidacion = findViewById(R.id.layout_botones_validacion)
        tvEstatusValidacion = findViewById(R.id.tv_estatus_validacion)

        // 1. Recibir datos
        documentId = intent.getStringExtra("EXTRA_DOCUMENT_ID") ?: run {
            Toast.makeText(this, "Error: DocumentId no proporcionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        infraccionId = intent.getIntExtra("EXTRA_ID", -1)
        val folio = intent.getStringExtra("EXTRA_FOLIO") ?: "N/A"
        val placa = intent.getStringExtra("EXTRA_PLACA") ?: "N/A"
        val fecha = intent.getStringExtra("EXTRA_FECHA") ?: "N/A"
        val ubicacion = intent.getStringExtra("EXTRA_UBICACION") ?: "N/A"
        val fotosUrls = intent.getStringArrayListExtra("EXTRA_FOTOS") ?: arrayListOf()
        val firmaUrl = intent.getStringExtra("EXTRA_FIRMA")

        // 2. Vincular vistas
        findViewById<TextView>(R.id.tv_detalle_folio).text = "Folio: $folio"
        findViewById<TextView>(R.id.tv_detalle_placa).text = "Placa: $placa"
        findViewById<TextView>(R.id.tv_detalle_fecha).text = "Fecha: ${fecha.substringBefore("T")}"
        findViewById<TextView>(R.id.tv_detalle_ubicacion).text = "Ubicación: $ubicacion"

        // 3. Cargar Imágenes de Evidencia
        val containerFotos = findViewById<android.widget.LinearLayout>(R.id.layout_imagenes_detalle)
        fotosUrls.forEach { url ->
            val imageView = ImageView(this).apply {
                layoutParams = android.widget.LinearLayout.LayoutParams(400, 400).apply {
                    setMargins(8, 0, 8, 0)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                load(BASE_URL_IMAGES + url) {
                    crossfade(true)
                    placeholder(android.R.drawable.ic_menu_gallery)
                    error(android.R.drawable.ic_menu_report_image)
                }
            }
            containerFotos.addView(imageView)
        }

        // 4. Cargar Firma
        firmaUrl?.let {
            findViewById<ImageView>(R.id.iv_firma_detalle).load(BASE_URL_IMAGES + it)
        }

        // 5. Botones de Validación
        findViewById<MaterialButton>(R.id.btn_aceptar).setOnClickListener { enviarValidacion(1) }
        findViewById<MaterialButton>(R.id.btn_rechazar).setOnClickListener { enviarValidacion(2) }

        // Log para debug
        android.util.Log.d("DETALLE_INFRACCION", "DocumentId recibido: $documentId")
        android.util.Log.d("DETALLE_INFRACCION", "InfraccionId recibido: $infraccionId")
    }

    private fun enviarValidacion(estado: Int) {
        if (documentId.isEmpty()) {
            Toast.makeText(this, "Error: DocumentId no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonUpdate = """
            {
              "data": {
                "validacion": $estado
              }
            }
        """.trimIndent()

        val body = jsonUpdate.toRequestBody("application/json".toMediaTypeOrNull())

        android.util.Log.d("ENVIAR_VALIDACION", "DocumentId: $documentId")
        android.util.Log.d("ENVIAR_VALIDACION", "Estado: $estado")
        android.util.Log.d("ENVIAR_VALIDACION", "JSON: $jsonUpdate")

        RetrofitSecureClient.infraccionApiService.actualizarInfraccion(documentId, body)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    android.util.Log.d("ENVIAR_VALIDACION", "Response code: ${response.code()}")
                    android.util.Log.d("ENVIAR_VALIDACION", "Response successful: ${response.isSuccessful}")

                    if (response.isSuccessful) {
                        val msj = if (estado == 1) "Aceptada" else "Rechazada"
                        Toast.makeText(this@DetalleInfraccionActivity, "Infracción $msj", Toast.LENGTH_SHORT).show()

                        // Usar las variables ya inicializadas
                        layoutBotonesValidacion.visibility = View.GONE
                        tvEstatusValidacion.visibility = View.VISIBLE
                        tvEstatusValidacion.text = "Estatus: $msj"

                        // Opcional: cerrar activity después de un tiempo
                        android.os.Handler(mainLooper).postDelayed({
                            finish()
                        }, 2000)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        android.util.Log.e("ENVIAR_VALIDACION", "Error response: $errorBody")
                        Toast.makeText(
                            this@DetalleInfraccionActivity,
                            "Error al actualizar: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    android.util.Log.e("ENVIAR_VALIDACION", "Error de conexión: ${t.message}")
                    Toast.makeText(
                        this@DetalleInfraccionActivity,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}