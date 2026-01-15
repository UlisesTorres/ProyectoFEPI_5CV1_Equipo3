package com.example.myapplication.view.supervisor

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private val BASE_URL_IMAGES = "https://apisistemainfracciones-production.up.railway.app"
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_infraccion)

        // 1. Recibir datos

        documentId = intent.getStringExtra("EXTRA_DOCUMENT_ID")
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
        val containerFotos = findViewById<LinearLayout>(R.id.layout_imagenes_detalle)
        fotosUrls.forEach { url ->
            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(400, 400).apply { setMargins(8, 0, 8, 0) }
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
    }

    private fun enviarValidacion(estado: Int) {
        if (infraccionId == -1) return

        val jsonUpdate = """
            {
              "data": {
                "validacion": $estado
              }
            }
        """.trimIndent()

        val body = jsonUpdate.toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.infraccionApiService.actualizarInfraccion(infraccionId, body)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val msj = if (estado == 1) "Aceptada" else "Rechazada"
                        Toast.makeText(this@DetalleInfraccionActivity, "Infracción $msj", Toast.LENGTH_SHORT).show()

                        // Ocultar botones tras validar
                        findViewById<LinearLayout>(R.id.layout_botones_validacion).visibility = View.GONE
                        val tvEstatus = findViewById<TextView>(R.id.tv_estatus_validacion)
                        tvEstatus.visibility = View.VISIBLE
                        tvEstatus.text = "Estatus: $msj"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@DetalleInfraccionActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }
}