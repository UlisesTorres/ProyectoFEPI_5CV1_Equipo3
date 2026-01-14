package com.example.myapplication.view.transito

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.transito.EvidenciaModel
import com.example.myapplication.presenter.transito.InfraccionesPresenter
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.Marker
import com.example.myapplication.model.transito.TipoInfraccionDTO

class InfraccionesActivity : AppCompatActivity(), InfraccionesContract.View {

    // Vistas Existentes
    private lateinit var spinnerInfracciones: Spinner
    private lateinit var mapView: MapView
    private lateinit var tvDireccion: TextView
    private lateinit var tvFecha: TextView

    // --- NUEVAS VISTAS ---
    private lateinit var etPlacas: EditText
    private lateinit var tvTipo: TextView
    private lateinit var tvMarca: TextView
    private lateinit var tvModelo: TextView
    private lateinit var tvColor: TextView
    private lateinit var btnConsultarPlaca: Button

    private lateinit var etLicencia: EditText
    private lateinit var tvNombreConductor: TextView
    private lateinit var tvEstatusLicencia: TextView
    private lateinit var btnValidarLicencia: Button

    // Card 3: Nueva vista de artículo
    private lateinit var tvArticuloInfraccion: TextView

    // Mapa
    private var mapLibreMap: MapLibreMap? = null
    private var marcadorInfraccion: Marker? = null

    // MVP
    private lateinit var presenter: InfraccionesContract.Presenter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this)
        setContentView(R.layout.activity_infracciones)

        // 1. Inicializar Vistas
        tvFecha = findViewById(R.id.tvFFija)
        spinnerInfracciones = findViewById(R.id.spinnerInfracciones)
        mapView = findViewById(R.id.map)
        tvDireccion = findViewById(R.id.tvDireccionInfraccion)

        // Card 1
        etPlacas = findViewById(R.id.etPlaca)
        tvTipo = findViewById(R.id.tvTipo)
        tvMarca = findViewById(R.id.tvMarca)
        tvModelo = findViewById(R.id.tvModelo)
        tvColor = findViewById(R.id.tvColor)
        btnConsultarPlaca = findViewById(R.id.btnConsultarPlaca)

        // Card 2
        etLicencia = findViewById(R.id.etLicencia)
        tvNombreConductor = findViewById(R.id.tvNombreConductor)
        tvEstatusLicencia = findViewById(R.id.tvEstatusLicencia)
        btnValidarLicencia = findViewById(R.id.btnValidarLicencia)

        // Card 3: Articulo (La que acabas de agregar al XML)
        tvArticuloInfraccion = findViewById(R.id.tvArticuloInfraccion)

        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnMyLocation = findViewById<ImageButton>(R.id.btnMyLocation)

        // 2. Inicializar MVP
        val model = EvidenciaModel(this)
        presenter = InfraccionesPresenter(this, model)
        presenter.cargarCatalogoInfracciones()

        // 3. Configuración Inicial
        presenter.cargarFechaInfraccion()
        setupSpinner()
        setupMapa(savedInstanceState)

        // 4. Listeners
        btnConsultarPlaca.setOnClickListener {
            val placa = etPlacas.text.toString()
            if (placa.isNotEmpty()) {
                mostrarMensaje("Consultando placa: $placa")
            } else {
                etPlacas.error = "Ingrese una placa"
            }
        }

        btnValidarLicencia.setOnClickListener {
            val licencia = etLicencia.text.toString()
            if (licencia.isNotEmpty()) {
                tvEstatusLicencia.text = "Estado: Validando..."
            } else {
                etLicencia.error = "Ingrese licencia"
            }
        }

        btnSiguiente.setOnClickListener {
            val placas = etPlacas.text.toString()
            val tipoInfraccion = spinnerInfracciones.selectedItem.toString()
            presenter.validarYGuardarInfraccion(placas, tipoInfraccion)
        }

        btnMyLocation.setOnClickListener {
            mapLibreMap?.getStyle { style -> activarUbicacion(style) }
        }

        mapView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    private fun setupSpinner() {
        val infracciones = listOf("Exceso de velocidad", "Lugar prohibido", "Semáforo", "Celular")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, infracciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInfracciones.adapter = adapter

        // Listener para actualizar el TextView del artículo según la selección
        spinnerInfracciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val seleccionado = infracciones[position]

                // Ejemplo de lógica para mostrar el artículo (Esto podría venir de tu Presenter/Model)
                val textoArticulo = when (seleccionado) {
                    "Exceso de velocidad" -> "Artículo 9: Circular a velocidad superior a la permitida."
                    "Lugar prohibido" -> "Artículo 30: Estacionar en zonas restringidas o señalizadas."
                    "Semáforo" -> "Artículo 10: No respetar la luz roja del semáforo."
                    "Celular" -> "Artículo 38: Uso de dispositivos electrónicos al conducir."
                    else -> "Artículo aplicable: --"
                }
                tvArticuloInfraccion.text = textoArticulo
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun mostrarCatalogoInfracciones(lista: List<TipoInfraccionDTO>) {

        val listaValida = lista.filter { !it.nombre.isNullOrBlank() }

        val nombres = listaValida.map { it.nombre!! }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombres
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInfracciones.adapter = adapter

        spinnerInfracciones.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val infraccion = listaValida[position]

                    tvArticuloInfraccion.text =
                        infraccion.descripcion ?: "Descripción no disponible"
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }


    // --- MÉTODOS DE CONTRATO (SIN TOCAR SEGÚN INSTRUCCIÓN) ---
    private fun setupMapa(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.mapLibreMap = map
            val absolutePath = EvidenciaModel(this).prepararMapaLocal()
            val styleJson = assets.open("cdmx_style.json").bufferedReader().use { it.readText() }
            val finalStyle = styleJson.replace("{file_path}", absolutePath)
            map.setStyle(org.maplibre.android.maps.Style.Builder().fromJson(finalStyle)) { style ->
                val cdmxCenter = LatLng(19.4326, -99.1332)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(cdmxCenter, 14.0))
                map.addOnMapClickListener { point ->
                    presenter.procesarClickMapa(point)
                    true
                }
            }
        }
    }

    override fun actualizarDireccionEnPantalla(direccion: String) {
        tvDireccion.setText(direccion)
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

    override fun navegarAEvidencia(placas: String, direccion: String, fechaISO: String) {
        val intent = Intent(this, EvidenciaActivity::class.java)
        intent.putExtra("PLACAS", placas)
        intent.putExtra("DIRECCION", direccion)
        intent.putExtra("FECHA", fechaISO)
        startActivity(intent)
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun limpiarFormulario() {
        etPlacas.setText("")
        etLicencia.setText("")
        tvNombreConductor.text = "--"
        tvEstatusLicencia.text = "Estado: Pendiente"
        tvArticuloInfraccion.text = "Artículo aplicable: --"
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

    // Ciclo de vida del MapView
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