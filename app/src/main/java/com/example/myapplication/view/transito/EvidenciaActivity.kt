package com.example.myapplication.view.transito

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
    private var contadorFotos = 0
    private lateinit var fotoActualUri: Uri
    private val listaArchivosFotos = mutableListOf<File>()

    private var placas: String? = null
    private var direccion: String? = null
    private var fechaInfraccion: String? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) procesarYMostrarFoto()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evidencia)

        placas = intent.getStringExtra("PLACAS")
        direccion = intent.getStringExtra("DIRECCION")
        fechaInfraccion = intent.getStringExtra("FECHA")

        layoutFotos = findViewById(R.id.layoutFotosEvidencia)
        signaturePad = findViewById(R.id.signaturePad)

        // 🔥 BLOQUEO TOTAL DE GUARDADO DE ESTADO
        layoutFotos.isSaveEnabled = false
        layoutFotos.isSaveFromParentEnabled = false
        signaturePad.isSaveEnabled = false
        signaturePad.isSaveFromParentEnabled = false

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
            if (signaturePad.isEmpty) {
                Toast.makeText(this, "La firma es obligatoria", Toast.LENGTH_LONG).show()
            } else if (contadorFotos == 0) {
                Toast.makeText(this, "Debes incluir al menos una foto", Toast.LENGTH_SHORT).show()
            } else {
                val archivoFirma = prepararArchivoFirma()
                enviarEvidenciaAlServidor(placas, direccion, archivoFirma, listaArchivosFotos)
            }
        }
    }

    private fun crearUriFoto(): Uri {
        val archivo = File(
            getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
            "FOTO_${System.currentTimeMillis()}.jpg"
        )
        listaArchivosFotos.add(archivo)
        return FileProvider.getUriForFile(this, "$packageName.fileprovider", archivo)
    }


    private fun procesarYMostrarFoto() {
        try {
            val inputStream = contentResolver.openInputStream(fotoActualUri)
            val options = BitmapFactory.Options().apply { inSampleSize = 4 }
            var bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            if (bitmap != null) {
                bitmap = corregirRotacion(bitmap, fotoActualUri)
                val contenedor = FrameLayout(this)
                contenedor.isSaveEnabled = false
                val nombreArchivo = fotoActualUri.lastPathSegment ?: ""
                val archivoAsociado = File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), nombreArchivo)
                contenedor.tag = archivoAsociado

                val layoutParams = LinearLayout.LayoutParams(250, 500)
                layoutParams.setMargins(10, 10, 10, 10)
                contenedor.layoutParams = layoutParams

                val ivFoto = ImageView(this)
                ivFoto.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                ivFoto.scaleType = ImageView.ScaleType.CENTER_CROP
                ivFoto.setImageBitmap(bitmap)
                ivFoto.isSaveEnabled = false

                val btnCerrar = ImageButton(this)
                val btnParams = FrameLayout.LayoutParams(70, 70)
                btnParams.gravity = Gravity.TOP or Gravity.END
                btnCerrar.layoutParams = btnParams
                btnCerrar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                btnCerrar.setBackgroundResource(android.R.drawable.presence_offline)

                btnCerrar.setOnClickListener {
                    val archivoABorrar = contenedor.tag as? File
                    archivoABorrar?.let { listaArchivosFotos.remove(it) }
                    layoutFotos.removeView(contenedor)
                    contadorFotos--
                }

                contenedor.addView(ivFoto)
                contenedor.addView(btnCerrar)
                layoutFotos.addView(contenedor, 0)
                contadorFotos++
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
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

    private fun enviarEvidenciaAlServidor(placas: String?, direccion: String?, firma: File, fotos: List<File>) {
        val progressDialog = AlertDialog.Builder(this)
            .setView(ProgressBar(this).apply { setPadding(40, 40, 40, 40) })
            .setTitle("Guardando Infracción")
            .setMessage("Conectando con el servidor...")
            .setCancelable(false)
            .show()

        val folio = "INF-${System.currentTimeMillis()}"
        val jsonString = """
            {
              "data": {
                "folio": "$folio",
                "placa_vehiculo": "${placas ?: "S/P"}",
                "ubicacion_infraccion": "${direccion ?: "N/D"}",
                "fecha_infraccion": "$fechaInfraccion",
                "medio_infraccion": "policia_transito"
              }
            }
        """.trimIndent()

        val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitSecureClient.infraccionApiService.crearInfraccion(body).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val json = JSONObject(response.body()!!.string())
                    val infraccionId = json.getJSONObject("data").getInt("id")
                    subirArchivo(infraccionId, firma, "firma_infractor")
                    fotos.forEach { foto -> subirArchivo(infraccionId, foto, "evidencia_infraccion") }
                    finalizarActividad("Infracción enviada correctamente")
                } else {
                    guardarOffline(folio, placas, direccion, firma, fotos)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
                guardarOffline(folio, placas, direccion, firma, fotos)
            }
        })
    }

    private fun guardarOffline(folio: String, placas: String?, direccion: String?, firma: File, fotos: List<File>) {
        val rutasFotos = fotos.joinToString(",") { it.absolutePath }
        val infraccion = InfraccionPendiente(
            folio = folio,
            placa = placas ?: "S/P",
            ubicacion = direccion ?: "N/D",
            fecha = fechaInfraccion ?: "",
            rutaFirma = firma.absolutePath,
            rutasFotos = rutasFotos
        )

        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(applicationContext).infraccionDao().insertar(infraccion)
            programarSincronizacion()
            withContext(Dispatchers.Main) {
                finalizarActividad("Sin conexión. Se guardó localmente y se enviará al recuperar internet.")
            }
        }
    }

    private fun programarSincronizacion() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "sync_infracciones",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
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

    override fun onStop() {
        super.onStop()

        for (i in 0 until layoutFotos.childCount) {
            val contenedor = layoutFotos.getChildAt(i) as? FrameLayout ?: continue
            val iv = contenedor.getChildAt(0) as? ImageView ?: continue
            iv.setImageDrawable(null)
        }

        layoutFotos.removeAllViews()
    }
}
