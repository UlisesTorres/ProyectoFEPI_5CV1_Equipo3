package com.example.myapplication.view.infracciones

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.myapplication.R
import com.example.myapplication.view.infracciones.TransitoActivity
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.File

class EvidenciaActivity : ComponentActivity() {

    private lateinit var layoutFotos: LinearLayout
    private lateinit var signaturePad: SignaturePad
    private var contadorFotos = 0
    private lateinit var fotoActualUri: Uri

    // CORRECCIÓN 1: Declarar estas variables aquí arriba para que funcionen en toda la clase
    private var placas: String? = null
    private var direccion: String? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) procesarYMostrarFoto()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evidencia)

        // Recuperar datos del intent
        placas = intent.getStringExtra("PLACAS")
        direccion = intent.getStringExtra("DIRECCION")

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
                Toast.makeText(this, "La firma es obligatoria para proceder", Toast.LENGTH_LONG).show()
            } else if (contadorFotos == 0) {
                Toast.makeText(this, "Debes incluir al menos una foto de evidencia", Toast.LENGTH_SHORT).show()
            } else {
                val mensaje = "Guardando infracción de placas: $placas..."
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

                btnFinalizar.isEnabled = false

                btnFinalizar.postDelayed({
                    // 1. Creamos el Intent hacia la pantalla principal (Oficial de Tránsito)
                    val intent = android.content.Intent(this, TransitoActivity::class.java)

                    // 2. Estas banderas limpian todas las actividades anteriores
                    // Así, si el usuario presiona "atrás" en el menú, no regresará a la cámara
                    intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                    finish() // Cerramos esta actividad
                }, 2000)
            }
        }
    }

    private fun crearUriFoto(): Uri {
        val archivo = File(getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "FOTO_${System.currentTimeMillis()}.jpg")
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

                btnCerrar.setOnClickListener {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(Bundle())
    }
}