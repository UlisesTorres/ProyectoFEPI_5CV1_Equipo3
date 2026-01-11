package com.example.myapplication.view.transito

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.infracciones.InfraccionesModel
import com.example.myapplication.presenter.transito.InfraccionesPresenter
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.Marker

class InfraccionesActivity : AppCompatActivity(), InfraccionesContract.View {

    // Vistas
    private lateinit var spinnerInfracciones: Spinner
    private lateinit var mapView: MapView
    private lateinit var tvDireccion: TextView
    private lateinit var tvFecha: TextView
    private lateinit var etPlacas: EditText


    // Mapa
    private var mapLibreMap: MapLibreMap? = null
    private var marcadorInfraccion: Marker? = null

    // MVP: Usamos la interfaz del contrato
    private lateinit var presenter: InfraccionesContract.Presenter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuración de MapLibre y Diseño
        MapLibre.getInstance(this)
        setContentView(R.layout.activity_infracciones)

        // 2. Inicializar Vistas
        tvFecha = findViewById(R.id.tvFFija)
        spinnerInfracciones = findViewById(R.id.spinnerInfracciones)
        mapView = findViewById(R.id.map)
        tvDireccion = findViewById(R.id.tvDireccionInfraccion)
        etPlacas = findViewById(R.id.editTextText2)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnMyLocation = findViewById<ImageButton>(R.id.btnMyLocation)

        // 3. Inicializar MVP
        // Nota: Asegúrate de que InfraccionesModel esté en el paquete correcto
        val model = InfraccionesModel(this)
        presenter = InfraccionesPresenter(this, model)

        // 4. Configuración Inicial
        presenter.cargarFechaInfraccion()
        setupSpinner()
        setupMapa(savedInstanceState)

        // 5. Listeners de Usuario
        btnSiguiente.setOnClickListener {
            val placas = etPlacas.text.toString()
            val tipo = spinnerInfracciones.selectedItem.toString()
            // El presenter decide si los datos son válidos
            presenter.validarYGuardarInfraccion(placas, tipo)

        }

        btnMyLocation.setOnClickListener {
            mapLibreMap?.getStyle { style -> activarUbicacion(style) }
        }

        // Bloqueo de scroll para que el mapa no se mueva al hacer scroll en la pantalla
        mapView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    private fun setupMapa(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.mapLibreMap = map

            // Obtenemos el estilo local a través del modelo
            val absolutePath = InfraccionesModel(this).prepararMapaLocal()
            val styleJson = assets.open("cdmx_style.json").bufferedReader().use { it.readText() }
            val finalStyle = styleJson.replace("{file_path}", absolutePath)

            map.setStyle(org.maplibre.android.maps.Style.Builder().fromJson(finalStyle)) { style ->
                val cdmxCenter = LatLng(19.4326, -99.1332)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(cdmxCenter, 14.0))

                // Click en el mapa para situar la infracción
                map.addOnMapClickListener { point ->
                    presenter.procesarClickMapa(point)
                    true
                }
            }
        }
    }

    // --- IMPLEMENTACIÓN DEL CONTRATO (View) ---

    override fun actualizarDireccionEnPantalla(direccion: String) {
        tvDireccion.text = direccion
    }

    override fun moverMarcador(latLng: LatLng) {
        marcadorInfraccion?.let { mapLibreMap?.removeMarker(it) }
        marcadorInfraccion = mapLibreMap?.addMarker(
            MarkerOptions().position(latLng).title("Lugar de Infracción")
        )
    }

    override fun mostrarFechaActual(fecha: String) {
        tvFecha.text = fecha
    }

    override fun navegarAEvidencia(placas: String, direccion: String) {
        val intent = Intent(this, EvidenciaActivity::class.java)
        intent.putExtra("PLACAS", placas)
        intent.putExtra("DIRECCION", direccion)
        startActivity(intent)
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun limpiarFormulario() {
        etPlacas.setText("")
        spinnerInfracciones.setSelection(0)
    }

    override fun bloquearEnvio() {
        findViewById<Button>(R.id.btnSiguiente).isEnabled = false
    }

    override fun ocultarTeclado() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // --- LÓGICA DE UBICACIÓN Y CICLO DE VIDA ---

    private fun activarUbicacion(style: org.maplibre.android.maps.Style) {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationComponent = mapLibreMap?.locationComponent
            val options = org.maplibre.android.location.LocationComponentActivationOptions
                .builder(this, style)
                .useDefaultLocationEngine(true)
                .build()

            locationComponent?.activateLocationComponent(options)
            locationComponent?.isLocationComponentEnabled = true
            locationComponent?.cameraMode = org.maplibre.android.location.modes.CameraMode.TRACKING
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



    // Ciclo de vida del MapView (Vital para que no se trabe)
    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() { super.onStop(); mapView.onStop() }
    override fun onDestroy() {
        mapLibreMap = null
        mapView.onDestroy()
        super.onDestroy()
    }
}