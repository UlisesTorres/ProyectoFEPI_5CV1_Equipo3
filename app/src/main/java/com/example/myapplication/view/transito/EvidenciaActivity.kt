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

    // Variables para almacenar los datos recibidos
    private var tipoVehiculo: String? = null
    private var marcaVehiculo: String? = null
    private var modeloVehiculo: String? = null
    private var colorVehiculo: String? = null
    private var nombreConductor: String? = null

    private var esVehiculoManual = false
    private var esConductorManual = false
    private var pagoId = 0


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


        recibirDatosInfraccion()


        // Inicializar vistas
        layoutFotos = findViewById(R.id.layoutFotosEvidencia)
        signaturePad = findViewById(R.id.signaturePad)
        scrollFotos = findViewById(R.id.scrollFotos)

        // Configuración de bloqueo de estado (Evita comportamientos extraños en recreación)
        layoutFotos.isSaveEnabled = false
        signaturePad.isSaveEnabled = false

        Log.d("DEPURACION", "Vehiculo Manual: $esVehiculoManual")
        Log.d("DEPURACION", "Conductor Manual: $esConductorManual")
        Log.d("DEPURACION", "Marca: $marcaVehiculo")

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
            findViewById<Button>(R.id.btnFinalizarInfraccion).setOnClickListener {
                // 1️⃣ Validaciones
                if (signaturePad.isEmpty) {
                    Toast.makeText(this, "La firma es obligatoria", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (contadorFotos == 0) {
                    Toast.makeText(this, "Debes incluir al menos una foto", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Preparar archivo de firma
                val archivoFirma = prepararArchivoFirma()

                // Mostrar ProgressDialog
                val progressDialog = AlertDialog.Builder(this)
                    .setView(ProgressBar(this).apply { setPadding(40, 40, 40, 40) })
                    .setTitle("Guardando Infracción")
                    .setMessage("Generando línea de captura y enviando datos...")
                    .setCancelable(false)
                    .show()

                // 2️⃣ Crear el pago primero
                crearPago(
                    onSuccess = { pagoId, lineaCaptura ->
                        // 3️⃣ Con el ID del pago, crear la infracción
                        val vehiculoIdExistente = if (!esVehiculoManual) intent.getIntExtra("VEHICULO_ID", -1) else null
                        val licenciaIdExistente = if (!esConductorManual) intent.getIntExtra("LICENCIA_ID", -1) else null

                        crearInfraccionConPago(
                            progressDialog,
                            pagoId,
                            lineaCaptura,
                            placas,
                            direccion,
                            archivoFirma,
                            listaArchivosFotos,
                            vehiculoIdExistente?.takeIf { it != -1 },
                            licenciaIdExistente?.takeIf { it != -1 }
                        )
                    },
                    onError = {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "No se pudo generar la línea de captura",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }

        }
    }

    private fun recibirDatosInfraccion() {

        // 3. Datos del Vehículo (Vienen de Spinner o TextView según la lógica anterior)
        tipoVehiculo = intent.getStringExtra("VEHICULO_TIPO")
        marcaVehiculo = intent.getStringExtra("VEHICULO_MARCA")
        modeloVehiculo = intent.getStringExtra("VEHICULO_MODELO")
        colorVehiculo = intent.getStringExtra("VEHICULO_COLOR")

        // 4. Datos del Conductor
        nombreConductor = intent.getStringExtra("CONDUCTOR_NOMBRE")

        esVehiculoManual = intent.getBooleanExtra("ES_FORANEO", false)
        esConductorManual = intent.getBooleanExtra("ES_MANUAL_CONDUCTOR", false)
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
            return
        }
        if (contadorFotos == 0) {
            Toast.makeText(this, "Debes incluir al menos una foto", Toast.LENGTH_SHORT).show()
            return
        }

        if (esVehiculoManual || esConductorManual) {
            // RUTA B: Registra en tablas externas y luego llama a enviarEvidenciaAlServidor
            registrarDatosNuevosYCrearInfraccion()
        } else {
            // RUTA A: Usamos los IDs que ya existían (recuperados por el intent)
            val vehiculoIdExistente = intent.getIntExtra("VEHICULO_ID", -1)
            val licenciaIdExistente = intent.getIntExtra("LICENCIA_ID", -1)

            val archivoFirma = prepararArchivoFirma()

            // Enviamos directamente con los IDs que ya tenemos
            enviarEvidenciaAlServidor(
                placas,
                direccion,
                archivoFirma,
                listaArchivosFotos,
                if (vehiculoIdExistente != -1) vehiculoIdExistente else null,
                if (licenciaIdExistente != -1) licenciaIdExistente else null
            )
        }
    }

    private fun registrarDatosNuevosYCrearInfraccion() {
        val progressDialog = AlertDialog.Builder(this)
            .setTitle("Sincronizando")
            .setMessage("Registrando datos manuales...")
            .setCancelable(false)
            .show()

        lifecycleScope.launch(Dispatchers.IO) {
            // Ejecutamos las inserciones en paralelo o secuencia
            if (esVehiculoManual) crearVehiculoEnStrapi()
            if (esConductorManual) crearLicenciaEnStrapi()

            // Una vez terminadas (exitosas o no), enviamos la infracción
            withContext(Dispatchers.Main) {
                progressDialog.dismiss()
                val archivoFirma = prepararArchivoFirma()
                // Llamas a tu función original tal cual la tenías
                enviarEvidenciaAlServidor(placas, direccion, archivoFirma, listaArchivosFotos)
            }
        }
    }

    private suspend fun crearVehiculoEnStrapi(): Boolean {
        val json = JSONObject().apply {
            put("data", JSONObject().apply {
                put("placa_id", placas)
                put("marca", marcaVehiculo)
                put("modelo", modeloVehiculo)
                put("color", colorVehiculo)
                put("tipo", tipoVehiculo)
            })
        }
        return try {
            val response = RetrofitSecureClient.vehiculoApiService.crearVehiculo(
                json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error Vehiculo: ${e.message}")
            false
        }
    }

    private suspend fun crearLicenciaEnStrapi(): Boolean {
        val json = JSONObject().apply {
            put("data", JSONObject().apply {
                put("num_licencia", "MANUAL-${System.currentTimeMillis()}")
                put("nombre", nombreConductor)
                // Según tu captura, podrías agregar apellido si lo tienes
            })
        }
        return try {
            val response = RetrofitSecureClient.licenciaApiService.crearLicencia(
                json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error Licencia: ${e.message}")
            false
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


    private fun enviarEvidenciaAlServidor(
        placas: String?,
        direccion: String?,
        firma: File,
        fotos: List<File>,
        vehiculoId: Int? = null,
        licenciaId: Int? = null,
    ) {
        val progressDialog = AlertDialog.Builder(this)
            .setView(ProgressBar(this).apply { setPadding(40, 40, 40, 40) })
            .setTitle("Guardando Infracción")
            .setMessage("Enviando datos...")
            .setCancelable(false)
            .show()

        val folio = "INF-${System.currentTimeMillis()}"

        val tipoInfraccionId = tipoInfraccionesIds.firstOrNull()
        val articuloId = articulosSeleccionadosIds.firstOrNull()

        val jsonBody = JSONObject().apply {
            put("data", JSONObject().apply {
                put("folio", folio) // folio para identificación de multa
                put("placa_vehiculo", placas ?: "S/P")
                put("ubicacion_infraccion", direccion ?: "N/D")
                put("fecha_infraccion", fechaInfraccion)
                put("medio_infraccion", "policia_transito")
                put("validacion", 0)
                put("oficial_id", usuarioId.toString())

                // 🔥 RELACIÓN UNO A UNO: pago
                put("pago", pagoId)

                // IDs de relaciones opcionales
                if (vehiculoId != null) put("vehiculo", vehiculoId)
                if (licenciaId != null) put("licencia", licenciaId)
                if (tipoInfraccionId != null) put("tipo_infraccion_id", tipoInfraccionId)
                if (articuloId != null) put("articulo_id", articuloId)
            })
        }

        Log.d("JSON_ENVIADO", jsonBody.toString())

        val body = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.infraccionApiService.crearInfraccion(body)
            .enqueue(object : retrofit2.Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful && response.body() != null) {
                        try {
                            val json = JSONObject(response.body()!!.string())
                            val infraccionId = json.getJSONObject("data").getInt("id") // ESTE ES EL INFRACCION_ID
                            // NO tocarlo, es independiente y vital

                            subirArchivo(infraccionId, firma, "firma_infractor")
                            fotos.forEach { subirArchivo(infraccionId, it, "evidencia_infraccion") }

                            finalizarActividad("Infracción creada con línea de captura")
                        } catch (e: Exception) {
                            Log.e("UPLOAD_ERROR", "Error parsing response", e)
                            Toast.makeText(this@EvidenciaActivity, "Error al procesar respuesta", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UPLOAD_ERROR", "Error: ${response.code()} - $errorBody")
                        Toast.makeText(this@EvidenciaActivity, "Error ${response.code()} al enviar datos", Toast.LENGTH_LONG).show()
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

    private fun crearPago(
        onSuccess: (pagoId: Int, lineaCaptura: String) -> Unit,
        onError: () -> Unit
    ) {
        val lineaCaptura = "LC-${System.currentTimeMillis()}"

        val jsonBody = JSONObject().apply {
            put("data", JSONObject().apply {
                put("linea_captura", lineaCaptura)
                put("estatus", "pendiente")
                put("monto", 0)
            })
        }

        val body = jsonBody.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.pagoApiService.crearPago(body)
            .enqueue(object : retrofit2.Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val json = JSONObject(response.body()!!.string())
                        val pagoId = json.getJSONObject("data").getInt("id")
                        onSuccess(pagoId, lineaCaptura)
                    } else {
                        Log.e("PAGO_ERROR", response.errorBody()?.string() ?: "")
                        onError()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("PAGO_ERROR", "Network error", t)
                    onError()
                }
            })
    }

    private fun crearInfraccionConPago(
        progressDialog: AlertDialog,
        pagoId: Int,
        lineaCaptura: String,
        placas: String?,
        direccion: String?,
        firma: File,
        fotos: List<File>,
        vehiculoId: Int?,
        licenciaId: Int?
    ) {

        val tipoInfraccionId = tipoInfraccionesIds.firstOrNull()
        val articuloId = articulosSeleccionadosIds.firstOrNull()

        val jsonBody = JSONObject().apply {
            put("data", JSONObject().apply {
                put("folio", lineaCaptura) // usamos la línea de captura
                put("placa_vehiculo", placas ?: "S/P")
                put("ubicacion_infraccion", direccion ?: "N/D")
                put("fecha_infraccion", fechaInfraccion)
                put("medio_infraccion", "policia_transito")
                put("validacion", 0)
                put("oficial_id", usuarioId.toString())

                // 🔥 RELACIÓN UNO A UNO
                put("pago_id", pagoId)

                if (vehiculoId != null) put("vehiculo", vehiculoId)
                if (licenciaId != null) put("licencia", licenciaId)
                if (tipoInfraccionId != null) put("tipo_infraccion_id", tipoInfraccionId)
                if (articuloId != null) put("articulo_id", articuloId)
            })
        }

        val body = jsonBody.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.infraccionApiService.crearInfraccion(body)
            .enqueue(object : retrofit2.Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    progressDialog.dismiss()

                    if (response.isSuccessful && response.body() != null) {
                        val json = JSONObject(response.body()!!.string())
                        val infraccionId = json.getJSONObject("data").getInt("id")

                        subirArchivo(infraccionId, firma, "firma_infractor")
                        fotos.forEach {
                            subirArchivo(infraccionId, it, "evidencia_infraccion")
                        }

                        finalizarActividad("Infracción creada con línea de captura")
                    } else {
                        Log.e("INF_ERROR", response.errorBody()?.string() ?: "")
                        Toast.makeText(
                            this@EvidenciaActivity,
                            "Error al crear infracción",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@EvidenciaActivity,
                        "Error de red",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }


}