package com.example.myapplication.view.infracciones

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.github.gcacace.signaturepad.views.SignaturePad
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.Marker
import java.io.File
import java.io.FileOutputStream

class InfraccionesActivity : ComponentActivity() {

    private lateinit var signaturePad: SignaturePad
    private lateinit var btnLimpiar: Button
    //private lateinit var btnEnviar: Button
    private lateinit var spinnerInfracciones: Spinner
    private lateinit var mapView: MapView

    private var mapLibreMap: MapLibreMap? = null
    private var marcadorInfraccion: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(this)
        setContentView(R.layout.activity_infracciones)

        // 1. Inicializar vistas (OJO: verifica que los IDs coincidan con tu XML)
        signaturePad = findViewById(R.id.SigFirma)
        btnLimpiar = findViewById(R.id.btnBorrarfirma)
        //btnEnviar = findViewById(R.id.btnEnviarInfraccion)
        spinnerInfracciones = findViewById(R.id.spinnerInfracciones)
        mapView = findViewById(R.id.map)

        setupSpinner()
        setupFirma()
        setupMapa(savedInstanceState)

        // Botón Enviar: Recupera datos, guarda firma y limpia
        //btnEnviar.setOnClickListener {
          //  procesarInfraccion()
        //}
    }

    private fun setupMapa(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.mapLibreMap = map

            val absolutePath = getMapFilePath()
            val styleJson = assets.open("cdmx_style.json").bufferedReader().use { it.readText() }
            val finalStyle = styleJson.replace("{file_path}", absolutePath)

            map.setStyle(org.maplibre.android.maps.Style.Builder().fromJson(finalStyle)) { style ->
                // Centro inicial: CDMX
                val cdmxCenter = LatLng(19.4326, -99.1332)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(cdmxCenter, 14.0))

                // Restaurar Marcador Inicial
                marcadorInfraccion = map.addMarker(MarkerOptions().position(cdmxCenter).title("Lugar de Infracción"))

                // Restaurar funcionalidad de Click en el mapa para mover el marcador
                map.addOnMapClickListener { point ->
                    marcadorInfraccion?.let { map.removeMarker(it) }
                    marcadorInfraccion = map.addMarker(MarkerOptions().position(point))
                    true
                }

                // Restaurar funcionalidad del botón de Mi Ubicación
                findViewById<ImageButton>(R.id.btnMyLocation).setOnClickListener {
                    activarUbicacion(style)
                }
            }
        }
    }

    private fun activarUbicacion(style: org.maplibre.android.maps.Style) {
        if (!style.isFullyLoaded) return
        val locationComponent = mapLibreMap?.locationComponent

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val options = org.maplibre.android.location.LocationComponentActivationOptions
                .builder(this, style)
                .useDefaultLocationEngine(true)
                .build()

            locationComponent?.activateLocationComponent(options)
            locationComponent?.isLocationComponentEnabled = true

            // Seguir al usuario con la cámara
            locationComponent?.cameraMode = org.maplibre.android.location.modes.CameraMode.TRACKING
            locationComponent?.renderMode = org.maplibre.android.location.modes.RenderMode.COMPASS
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }
    }

    private fun procesarInfraccion() {
        if (signaturePad.isEmpty) {
            Toast.makeText(this, "Firma requerida", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener datos
        val tipo = spinnerInfracciones.selectedItem.toString()
        val lat = marcadorInfraccion?.position?.latitude ?: 0.0
        val lng = marcadorInfraccion?.position?.longitude ?: 0.0

        // Guardar firma como imagen
        val firmaBitmap = signaturePad.signatureBitmap
        val file = File(filesDir, "firma_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out -> firmaBitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }

        Log.d("INFRACCION", "Tipo: $tipo | Ubicación: $lat, $lng | Archivo: ${file.absolutePath}")

        Toast.makeText(this, "Infracción guardada localmente", Toast.LENGTH_LONG).show()

        // Limpiar formulario después de enviar
        signaturePad.clear()
        spinnerInfracciones.setSelection(0)
    }

    private fun getMapFilePath(): String {
        val fileName = "mexico-city.mbtiles"
        val file = File(filesDir, fileName)
        if (!file.exists() || file.length() == 0L) {
            assets.open(fileName).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                    output.flush()
                }
            }
        }
        return file.absolutePath
    }

    private fun setupSpinner() {
        val infracciones = listOf("Exceso de velocidad", "Lugar prohibido", "Semáforo", "Celular")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, infracciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInfracciones.adapter = adapter
    }

    private fun setupFirma() {
        btnLimpiar.setOnClickListener { signaturePad.clear() }
    }

    // Ciclo de vida obligatorio para MapView
    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() { super.onStop(); mapView.onStop() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
}