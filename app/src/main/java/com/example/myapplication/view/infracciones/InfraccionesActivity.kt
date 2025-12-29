package com.example.myapplication.view.infracciones

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.infracciones.InfraccionesModel
import com.example.myapplication.presenter.infracciones.InfraccionesContract
import com.example.myapplication.presenter.infracciones.InfraccionesPresenter
import com.github.gcacace.signaturepad.views.SignaturePad
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.Marker

class InfraccionesActivity : ComponentActivity(), InfraccionesContract.View {

    private lateinit var signaturePad: SignaturePad
    private lateinit var btnLimpiar: Button
    private lateinit var spinnerInfracciones: Spinner
    private lateinit var mapView: MapView
    private lateinit var tvDireccion: TextView // Tu nuevo Label para la dirección

    private var mapLibreMap: MapLibreMap? = null
    private var marcadorInfraccion: Marker? = null

    private lateinit var tvFecha: TextView

    // MVP Variables
    private lateinit var presenter: InfraccionesPresenter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. SIEMPRE PRIMERO: Configurar el diseño y MapLibre
        MapLibre.getInstance(this)
        setContentView(R.layout.activity_infracciones)

        // 2. INICIALIZAR VISTAS (Después de setContentView)
        tvFecha = findViewById(R.id.tvFFija)
        signaturePad = findViewById(R.id.SigFirma)
        btnLimpiar = findViewById(R.id.btnBorrarfirma)
        spinnerInfracciones = findViewById(R.id.spinnerInfracciones)
        mapView = findViewById(R.id.map)
        tvDireccion = findViewById(R.id.tvDireccionInfraccion)

        // 3. INICIALIZAR MVP
        val model = InfraccionesModel(this)
        presenter = InfraccionesPresenter(this, model)

        // 4. USAR EL PRESENTER Y CONFIGURAR
        presenter.cargarFechaInfraccion() // Ahora sí, el presenter ya existe

        setupSpinner()
        setupFirma()
        setupMapa(savedInstanceState)

        // Bloqueadores de Scroll (Touch Listeners)
        mapView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        signaturePad.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    private fun setupMapa(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.mapLibreMap = map

            // El Modelo ahora nos da la ruta del archivo
            val absolutePath = InfraccionesModel(this).prepararMapaLocal()
            val styleJson = assets.open("cdmx_style.json").bufferedReader().use { it.readText() }
            val finalStyle = styleJson.replace("{file_path}", absolutePath)

            map.setStyle(org.maplibre.android.maps.Style.Builder().fromJson(finalStyle)) { style ->
                val cdmxCenter = LatLng(19.4326, -99.1332)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(cdmxCenter, 14.0))

                // Inicializamos marcador pidiéndole la dirección al Presenter
                presenter.procesarClickMapa(cdmxCenter)

                map.addOnMapClickListener { point ->
                    // AVISAMOS AL PRESENTER
                    presenter.procesarClickMapa(point)
                    true
                }

                findViewById<ImageButton>(R.id.btnMyLocation).setOnClickListener {
                    activarUbicacion(style)
                }
            }
        }
    }

    // --- IMPLEMENTACIÓN DE INFRACCIONESCONTRACT.VIEW ---

    override fun actualizarDireccionEnPantalla(direccion: String) {
        tvDireccion.text = direccion
    }

    override fun moverMarcador(latLng: LatLng) {
        marcadorInfraccion?.let { mapLibreMap?.removeMarker(it) }
        marcadorInfraccion = mapLibreMap?.addMarker(MarkerOptions().position(latLng).title("Lugar de Infracción"))
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun limpiarFormulario() {
        signaturePad.clear()
        spinnerInfracciones.setSelection(0)
    }

    override fun bloquearEnvio() {
        // btnEnviar.isEnabled = false
    }

    // --- MÉTODOS DE APOYO Y CICLO DE VIDA ---

    private fun activarUbicacion(style: org.maplibre.android.maps.Style) {
        if (!style.isFullyLoaded) return
        val locationComponent = mapLibreMap?.locationComponent

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // 1. Configuramos una petición de ubicación activa
            val request = org.maplibre.android.location.engine.LocationEngineRequest.Builder(1000L) // cada 1 segundo
                .setPriority(org.maplibre.android.location.engine.LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .build()

            val options = org.maplibre.android.location.LocationComponentActivationOptions
                .builder(this, style)
                .useDefaultLocationEngine(true)
                .locationEngineRequest(request) // Le decimos que no solo pida la última, sino que busque nuevas
                .build()

            try {
                locationComponent?.activateLocationComponent(options)
                locationComponent?.isLocationComponentEnabled = true

                // 2. Intentamos seguir al usuario
                locationComponent?.cameraMode = org.maplibre.android.location.modes.CameraMode.TRACKING
                locationComponent?.renderMode = org.maplibre.android.location.modes.RenderMode.COMPASS

            } catch (e: Exception) {
                Log.e("GPS_ERROR", "Error al activar componente: ${e.message}")
                mostrarMensaje("Esperando señal GPS...")
            }
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }
    }

    private fun setupSpinner() {
        val infracciones = listOf("Exceso de velocidad", "Lugar prohibido", "Semáforo", "Celular")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, infracciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInfracciones.adapter = adapter
    }

    private fun setupFirma() { btnLimpiar.setOnClickListener { signaturePad.clear() } }

    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() { super.onStop(); mapView.onStop() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
    override fun mostrarFechaActual(fecha: String) {
        tvFecha.text = fecha
    }
}