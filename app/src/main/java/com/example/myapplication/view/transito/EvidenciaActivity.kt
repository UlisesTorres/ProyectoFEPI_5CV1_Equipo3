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
import com.example.myapplication.R
import com.example.myapplication.network.InfraccionApiService
import com.example.myapplication.network.RetrofitSecureClient
import com.github.gcacace.signaturepad.views.SignaturePad
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EvidenciaActivity : ComponentActivity() {

    private lateinit var layoutFotos: LinearLayout
    private lateinit var signaturePad: SignaturePad
    private var contadorFotos = 0
    private lateinit var fotoActualUri: Uri
    private val listaArchivosFotos = mutableListOf<File>()
    private var userId: Int = -1

    // CORRECCIÓN 1: Declarar estas variables aquí arriba para que funcionen en toda la clase
    private var placas: String? = null
    private var direccion: String? = null
    private var fechaInfraccion: String? = null


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) procesarYMostrarFoto()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evidencia)
        userId = obtenerUserId()

        if (userId == -1) {
            Toast.makeText(this, "Sesión inválida, vuelve a iniciar sesión", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Recuperar datos del intent
        placas = intent.getStringExtra("PLACAS")
        direccion = intent.getStringExtra("DIRECCION")
        fechaInfraccion = intent.getStringExtra("FECHA")

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

        layoutFotos = findViewById(R.id.layoutFotosEvidencia)
        signaturePad = findViewById(R.id.signaturePad)

        findViewById<Button>(R.id.btnLimpiarFirma).setOnClickListener { signaturePad.clear() }

        findViewById<ImageButton>(R.id.btnTomarFoto).setOnClickListener {
            if (contadorFotos < 5) {
                fotoActualUri = crearUriFoto()
                cameraLauncher.launch(fotoActualUri)
            } else {
                Toast.makeText(this, "Máximo 5 fotos", Toast.LENGTH_SHORT).show()
            }
        }


        val btnFinalizar = findViewById<Button>(R.id.btnFinalizarInfraccion)
        btnFinalizar.setOnClickListener {
            if (signaturePad.isEmpty) {
                Toast.makeText(this, "La firma es obligatoria", Toast.LENGTH_LONG).show()
            } else if (contadorFotos == 0) {
                Toast.makeText(this, "Debes incluir al menos una foto", Toast.LENGTH_SHORT).show()
            } else {
                btnFinalizar.isEnabled = false // Bloqueamos para evitar doble clic

                val archivoFirma = prepararArchivoFirma()

                // Iniciamos el envío real
                enviarEvidenciaAlServidor(placas, direccion, archivoFirma, listaArchivosFotos)

                Toast.makeText(this, "Subiendo evidencia, por favor espere...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun crearUriFoto(): Uri {
        val archivo = File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "FOTO_${System.currentTimeMillis()}.jpg")
        listaArchivosFotos.add(archivo)
        return FileProvider.getUriForFile(this, "${packageName}.fileprovider", archivo)
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

                // --- NUEVO: Buscamos el archivo físico y lo guardamos en el TAG del contenedor ---
                val nombreArchivo = fotoActualUri.lastPathSegment ?: ""
                val archivoAsociado = File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), nombreArchivo)
                contenedor.tag = archivoAsociado

                val layoutParams = LinearLayout.LayoutParams(250, 500)
                layoutParams.setMargins(10, 10, 10, 10)
                contenedor.layoutParams = layoutParams

                val ivFoto = ImageView(this)
                ivFoto.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                ivFoto.scaleType = ImageView.ScaleType.CENTER_CROP
                ivFoto.setImageBitmap(bitmap)
                ivFoto.isSaveEnabled = false

                val btnCerrar = ImageButton(this)
                val btnParams = FrameLayout.LayoutParams(70, 70)
                btnParams.gravity = Gravity.TOP or Gravity.END
                btnCerrar.layoutParams = btnParams

                btnCerrar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                btnCerrar.setBackgroundResource(android.R.drawable.presence_offline)
                btnCerrar.setPadding(5, 5, 5, 5)

                // --- ACTUALIZADO: El botón ahora limpia la lista y la pantalla ---
                btnCerrar.setOnClickListener {
                    // 1. Obtenemos el archivo desde el tag
                    val archivoABorrar = contenedor.tag as? File

                    // 2. Lo quitamos de la lista global de envío
                    archivoABorrar?.let { listaArchivosFotos.remove(it) }

                    // 3. Quitamos la vista de la pantalla
                    layoutFotos.removeView(contenedor)
                    contadorFotos--

                    Toast.makeText(this, "Foto eliminada", Toast.LENGTH_SHORT).show()
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

    private fun obtenerUserId(): Int {
        val prefs = getSharedPreferences("AUTH_PREFS", MODE_PRIVATE)
        return prefs.getInt("user_id", -1)
    }

    private fun prepararArchivoFirma(): File {
        val archivoFirma = File(cacheDir, "firma_evidencia.png")
        val bitmapFirma = signaturePad.signatureBitmap

        archivoFirma.outputStream().use { out ->
            bitmapFirma.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return archivoFirma
    }
    private fun enviarEvidenciaAlServidor(
        placas: String?,
        direccion: String?,
        firma: File,
        fotos: List<File>
    ) {
        // 1️⃣ Crear y mostrar diálogo de carga (Bloquea la pantalla para evitar errores)
        val progressDialog = AlertDialog.Builder(this)
            .setView(ProgressBar(this).apply { setPadding(40, 40, 40, 40) })
            .setTitle("Guardando Infracción")
            .setMessage("Conectando con el servidor, por favor espere...")
            .setCancelable(false)
            .show()

        // 2️⃣ Preparar JSON
        val jsonString = """
    {
  "data": {
    "folio": "INF-${System.currentTimeMillis()}",
    "placa_vehiculo": "${placas ?: "S/P"}",
    "ubicacion_infraccion": "${direccion ?: "N/D"}",
    "fecha_infraccion": "$fechaInfraccion",
    "oficial_id": "$userId"
  }
}
""".trimIndent()

        val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

        // 3️⃣ Enviar a Strapi
        RetrofitSecureClient.infraccionApiService
            .crearInfraccion(body)
            .enqueue(object : retrofit2.Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    // Quitamos el círculo de carga
                    progressDialog.dismiss()

                    if (response.isSuccessful) {
                        // --- CASO ÉXITO ---
                        val json = JSONObject(response.body()!!.string())
                        val infraccionId = json.getJSONObject("data").getInt("id")

                        // Subir archivos en segundo plano
                        subirArchivo(infraccionId, firma, "firma_infractor")
                        fotos.forEach { foto -> subirArchivo(infraccionId, foto, "evidencia_infraccion") }

                        Toast.makeText(this@EvidenciaActivity, "Infracción guardada correctamente", Toast.LENGTH_LONG).show()

                        // NAVEGAR: Solo si fue exitoso
                        val intentPrincipal = Intent(this@EvidenciaActivity, TransitoActivity::class.java)
                        intentPrincipal.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intentPrincipal)
                        finish()
                    } else {
                        // --- CASO ERROR DE SERVIDOR (400, 403, 500, etc) ---
                        val errorBody = response.errorBody()?.string()
                        Log.e("STRAPI", "Error: $errorBody")

                        // Reactivamos el botón para que intente de nuevo
                        findViewById<Button>(R.id.btnFinalizarInfraccion).isEnabled = true

                        AlertDialog.Builder(this@EvidenciaActivity)
                            .setTitle("Error en el servidor")
                            .setMessage("No se pudo guardar la infracción. Revisa los datos o el rol del usuario.")
                            .setPositiveButton("Aceptar", null)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // --- CASO ERROR DE RED (Sin internet, servidor caído) ---
                    progressDialog.dismiss()
                    findViewById<Button>(R.id.btnFinalizarInfraccion).isEnabled = true

                    Log.e("STRAPI", "Fallo de conexión: ${t.message}")
                    Toast.makeText(this@EvidenciaActivity, "Error de red: No hay conexión con Railway", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun subirArchivo(
        infraccionId: Int,
        archivo: File,
        field: String
    ) {
        val filePart = MultipartBody.Part.createFormData(
            "files",
            archivo.name,
            archivo.asRequestBody(
                if (field == "firma_infractor")
                    "image/png".toMediaTypeOrNull()
                else
                    "image/jpeg".toMediaTypeOrNull()
            )
        )

        RetrofitSecureClient.uploadApiService.subirArchivo(
            filePart,
            "api::infraccion.infraccion".toRequestBody(),
            infraccionId.toString().toRequestBody(),
            field.toRequestBody()
        ).enqueue(object : retrofit2.Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (!response.isSuccessful) {
                    Log.e("UPLOAD", response.errorBody()?.string() ?: "")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("UPLOAD", t.message ?: "Error")
            }
        })
    }




    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(Bundle())
    }
}