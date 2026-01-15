package com.example.myapplication.view.transito

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.InfraccionPendiente

import com.example.myapplication.network.RetrofitSecureClient
import com.example.myapplication.worker.SyncWorker
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class EvidenciaActivity : ComponentActivity() {

    private lateinit var layoutFotos: LinearLayout
    private lateinit var signaturePad: SignaturePad
    private lateinit var scrollFotos: HorizontalScrollView
    private var contadorFotos = 0
    private lateinit var fotoActualUri: Uri
    private val listaArchivosFotos = mutableListOf<File>()
    private var usuarioId: Int = 0
    private var placas: String? = null
    private var direccion: String? = null
    private var fechaInfraccion: String? = null
    private var tipoInfraccionesIds: List<Int> = emptyList()

    private var articulosSeleccionadosIds: List<Int> = emptyList()

    // Launcher corregido: Solo agrega a la lista si la foto se tomó con éxito
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val nombreArchivo = fotoActualUri.lastPathSegment ?: ""
            val archivoReal = File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), nombreArchivo)
            if (!listaArchivosFotos.contains(archivoReal)) {
                listaArchivosFotos.add(archivoReal)
            }
            procesarYMostrarFoto()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evidencia)

        val prefs = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        usuarioId = prefs.getInt("user_id", 0)

        // Recuperar datos
        placas = intent.getStringExtra("PLACAS")
        direccion = intent.getStringExtra("DIRECCION")
        fechaInfraccion = intent.getStringExtra("FECHA")
        tipoInfraccionesIds = intent.getIntegerArrayListExtra("TIPO_INFRACCION_IDS") ?: emptyList()
        articulosSeleccionadosIds = intent.getIntegerArrayListExtra("ARTICULOS_IDS") ?: emptyList()


        // Inicializar vistas
        layoutFotos = findViewById(R.id.layoutFotosEvidencia)
        signaturePad = findViewById(R.id.signaturePad)
        scrollFotos = findViewById(R.id.scrollFotos)

        // Configuración de bloqueo de estado (Evita comportamientos extraños en recreación)
        layoutFotos.isSaveEnabled = false
        signaturePad.isSaveEnabled = false

        // Manejo del botón atrás
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@EvidenciaActivity)
                    .setTitle("¿Salir?")
                    .setMessage("Si regresas, las fotos y la firma se borrarán.")
                    .setPositiveButton("Salir") { _, _ -> finish() }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        })

        findViewById<Button>(R.id.btnLimpiarFirma).setOnClickListener {
            signaturePad.clear()
        }

        findViewById<ImageButton>(R.id.btnTomarFoto).setOnClickListener {
            if (contadorFotos < 5) {
                fotoActualUri = crearUriFoto()
                cameraLauncher.launch(fotoActualUri)
            } else {
                Toast.makeText(this, "Máximo 5 fotos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnFinalizarInfraccion).setOnClickListener {
            validarYEnviar()
        }
    }

    private fun crearUriFoto(): Uri {
        val archivo = File(
            getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
            "FOTO_${System.currentTimeMillis()}.jpg"
        )
        return FileProvider.getUriForFile(this, "$packageName.fileprovider", archivo)
    }

    private fun procesarYMostrarFoto() {
        try {
            val inputStream = contentResolver.openInputStream(fotoActualUri)
            val options = BitmapFactory.Options().apply { inSampleSize = 2 } // Mejor calidad que antes
            var bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            if (bitmap != null) {
                bitmap = corregirRotacion(bitmap, fotoActualUri)

                // Contenedor dinámico (FrameLayout)
                val contenedor = FrameLayout(this)
                val layoutParams = LinearLayout.LayoutParams(dpToPx(160), dpToPx(220))
                layoutParams.setMargins(dpToPx(8), 0, dpToPx(8), 0)
                contenedor.layoutParams = layoutParams

                // ImageView de la foto
                val ivFoto = ImageView(this)
                ivFoto.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                ivFoto.scaleType = ImageView.ScaleType.CENTER_CROP
                ivFoto.setImageBitmap(bitmap)

                // Botón para eliminar (La "X")
                val btnCerrar = ImageButton(this)
                val btnParams = FrameLayout.LayoutParams(dpToPx(40), dpToPx(40))
                btnParams.gravity = Gravity.TOP or Gravity.END
                btnCerrar.layoutParams = btnParams
                btnCerrar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                btnCerrar.setBackgroundResource(android.R.drawable.presence_busy) // Círculo rojo
                btnCerrar.setColorFilter(android.graphics.Color.WHITE)

                val nombreArchivo = fotoActualUri.lastPathSegment ?: ""
                val archivoAsociado = File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), nombreArchivo)

                btnCerrar.setOnClickListener {
                    listaArchivosFotos.remove(archivoAsociado)
                    layoutFotos.removeView(contenedor)
                    contadorFotos--
                }

                contenedor.addView(ivFoto)
                contenedor.addView(btnCerrar)

                // Agregar al final para que el scroll funcione de izquierda a derecha
                layoutFotos.addView(contenedor)
                contadorFotos++

                // Scroll automático a la derecha para ver la foto nueva
                scrollFotos.post { scrollFotos.fullScroll(View.FOCUS_RIGHT) }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al mostrar foto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarYEnviar() {
        if (signaturePad.isEmpty) {
            Toast.makeText(this, "La firma es obligatoria", Toast.LENGTH_LONG).show()
        } else if (contadorFotos == 0) {
            Toast.makeText(this, "Debes incluir al menos una foto", Toast.LENGTH_SHORT).show()
        } else {
            val archivoFirma = prepararArchivoFirma()
            enviarEvidenciaAlServidor(placas, direccion, archivoFirma, listaArchivosFotos)
        }
    }

    // --- FUNCIONES DE APOYO (Sin cambios significativos pero optimizadas) ---

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun corregirRotacion(bitmap: Bitmap, uri: Uri): Bitmap {
        val input = contentResolver.openInputStream(uri) ?: return bitmap
        val ei = ExifInterface(input)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        input.close()

        val angle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        if (angle == 0f) return bitmap
        val matrix = Matrix().apply { postRotate(angle) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun prepararArchivoFirma(): File {
        val archivoFirma = File(cacheDir, "firma_${System.currentTimeMillis()}.png")
        val bitmapFirma = signaturePad.signatureBitmap
        archivoFirma.outputStream().use { out ->
            bitmapFirma.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return archivoFirma
    }

    private fun formatearListaIdsParaStrapi(ids: List<Int>?): String {
        return ids?.joinToString(prefix = "[", postfix = "]") { """{"id":$it}""" } ?: "[]"
    }


    private fun enviarEvidenciaAlServidor(placas: String?, direccion: String?, firma: File, fotos: List<File>) {
        val progressDialog = AlertDialog.Builder(this)
            .setView(ProgressBar(this).apply { setPadding(40, 40, 40, 40) })
            .setTitle("Guardando Infracción")
            .setMessage("Enviando datos...")
            .setCancelable(false)
            .show()

        val folio = "INF-${System.currentTimeMillis()}"

        // Si son relaciones de uno a uno (solo un ID), toma el primero de la lista
        val tipoInfraccionId = tipoInfraccionesIds.firstOrNull()
        val articuloId = articulosSeleccionadosIds.firstOrNull()

        // Crear el JSON con el formato correcto para Strapi
        val jsonBody = JSONObject().apply {
            put("data", JSONObject().apply {
                put("folio", folio)
                put("placa_vehiculo", placas ?: "S/P")
                put("ubicacion_infraccion", direccion ?: "N/D")
                put("fecha_infraccion", fechaInfraccion)
                put("medio_infraccion", "policia_transito")
                put("validacion",0)
                put("oficial_id", usuarioId.toString())

                // Para relaciones de UNO a uno/muchos (usar ID directo)
                if (tipoInfraccionId != null) {
                    put("tipo_infraccion_id", tipoInfraccionId)
                } else {
                    // Puedes dejarlo null o no incluirlo
                    put("tipo_infraccion_id", JSONObject.NULL)
                }

                if (articuloId != null) {
                    put("articulo_id", articuloId)
                } else {
                    put("articulo_id", JSONObject.NULL)
                }
            })
        }

        // Convertir a String y log para debug
        val jsonString = jsonBody.toString()
        Log.d("JSON_ENVIADO", "JSON a enviar: $jsonString")

        val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.infraccionApiService.crearInfraccion(body).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    try {
                        val json = JSONObject(response.body()!!.string())
                        val infraccionId = json.getJSONObject("data").getInt("id")
                        subirArchivo(infraccionId, firma, "firma_infractor")
                        fotos.forEach { foto -> subirArchivo(infraccionId, foto, "evidencia_infraccion") }
                        finalizarActividad("Infracción enviada correctamente")
                    } catch (e: Exception) {
                        Log.e("UPLOAD_ERROR", "Error parsing response", e)
                        Toast.makeText(this@EvidenciaActivity, "Error al procesar respuesta", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UPLOAD_ERROR", "Error: ${response.code()} - $errorBody")

                    // Si todavía hay error, probar con formato de relación
                    if (response.code() == 400) {
                        // Probar con formato de relación (objeto con id)
                        enviarConFormatoRelacion(progressDialog, folio, placas, direccion, firma, fotos)
                    } else {
                        Toast.makeText(this@EvidenciaActivity, "Error ${response.code()} al enviar datos", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("UPLOAD_ERROR", "Network failure", t)
                Toast.makeText(this@EvidenciaActivity, "Error de conexión", Toast.LENGTH_LONG).show()
                guardarOffline(folio, tipoInfraccionesIds, articulosSeleccionadosIds, placas, direccion, firma, fotos)
            }
        })
    }

    private fun enviarConFormatoRelacion(
        progressDialog: AlertDialog,
        folio: String,
        placas: String?,
        direccion: String?,
        firma: File,
        fotos: List<File>
    ) {
        val tipoInfraccionId = tipoInfraccionesIds.firstOrNull()
        val articuloId = articulosSeleccionadosIds.firstOrNull()

        val jsonBody = JSONObject().apply {
            put("data", JSONObject().apply {
                put("folio", folio)
                put("placa_vehiculo", placas ?: "S/P")
                put("ubicacion_infraccion", direccion ?: "N/D")
                put("fecha_infraccion", fechaInfraccion)
                put("medio_infraccion", "policia_transito")

                // Formato como objeto de relación
                if (tipoInfraccionId != null) {
                    put("tipo_infraccion_id", JSONObject().apply {
                        put("id", tipoInfraccionId)
                    })
                }

                if (articuloId != null) {
                    put("articulo_id", JSONObject().apply {
                        put("id", articuloId)
                    })
                }
            })
        }

        val jsonString = jsonBody.toString()
        Log.d("JSON_ENVIADO_ALT", "JSON alternativo: $jsonString")

        val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.infraccionApiService.crearInfraccion(body).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    try {
                        val json = JSONObject(response.body()!!.string())
                        val infraccionId = json.getJSONObject("data").getInt("id")
                        subirArchivo(infraccionId, firma, "firma_infractor")
                        fotos.forEach { foto -> subirArchivo(infraccionId, foto, "evidencia_infraccion") }
                        finalizarActividad("Infracción enviada correctamente")
                    } catch (e: Exception) {
                        Log.e("UPLOAD_ERROR", "Error parsing response", e)
                        Toast.makeText(this@EvidenciaActivity, "Error al procesar respuesta", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UPLOAD_ERROR", "Error alternativo: ${response.code()} - $errorBody")

                    // Guardar offline como último recurso
                    guardarOffline(folio, tipoInfraccionesIds, articulosSeleccionadosIds, placas, direccion, firma, fotos)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
                guardarOffline(folio, tipoInfraccionesIds, articulosSeleccionadosIds, placas, direccion, firma, fotos)
            }
        })
    }

    private fun guardarOffline(
        folio: String,
        tipoInfraccionesIds: List<Int>,
        articulosSeleccionadosIds: List<Int>,
        placas: String?,
        direccion: String?,
        firma: File,
        fotos: List<File>
    ) {
        val rutasFotos = fotos.joinToString(",") { it.absolutePath }

        val tipoInfraccionId = tipoInfraccionesIds.firstOrNull() ?: 0
        val articuloId = articulosSeleccionadosIds.firstOrNull() ?: 0

        val infraccionPendiente = InfraccionPendiente(
            folio = folio,
            tipoInfraccionId = tipoInfraccionId,
            articuloId = articuloId,
            placa = placas ?: "S/P",
            ubicacion = direccion ?: "N/D",
            fecha = fechaInfraccion ?: "",
            rutaFirma = firma.absolutePath,
            rutasFotos = rutasFotos
        )

        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(applicationContext)
                .infraccionDao()
                .insertar(infraccionPendiente)

            programarSincronizacion()

            withContext(Dispatchers.Main) {
                finalizarActividad("Sin conexión. Se guardó localmente.")
            }
        }
    }


    private fun programarSincronizacion() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().setConstraints(constraints).build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork("sync_infracciones", ExistingWorkPolicy.KEEP, syncRequest)
    }

    private fun finalizarActividad(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        val intent = Intent(this, TransitoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun subirArchivo(infraccionId: Int, archivo: File, field: String) {
        val filePart = MultipartBody.Part.createFormData(
            "files", archivo.name,
            archivo.asRequestBody(if (field == "firma_infractor") "image/png".toMediaTypeOrNull() else "image/jpeg".toMediaTypeOrNull())
        )
        RetrofitSecureClient.uploadApiService.subirArchivo(
            filePart, "api::infraccion.infraccion".toRequestBody(),
            infraccionId.toString().toRequestBody(), field.toRequestBody()
        ).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {}
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    // 🔥 IMPORTANTE: SE ELIMINÓ ONSTOP() QUE BORRABA LAS VISTAS
}