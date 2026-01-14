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
import androidx.core.text.color
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
import com.example.myapplication.model.licencia.LicenciaDTO
import com.example.myapplication.model.vehiculo.VehiculoDTO

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
        setupMapa(savedInstanceState)

        // 4. Listeners
        btnConsultarPlaca.setOnClickListener {
            // ✅ SOLUCIÓN: Normalizamos la entrada antes de enviarla.
            // Convertimos a mayúsculas y quitamos espacios al principio y al final.
            val placaNormalizada = etPlacas.text.toString().uppercase().trim()

            // La vista solo debe hacer UNA cosa: delegar al presenter.
            presenter.onBotonConsultarPlacaPulsado(placaNormalizada)
        }

        btnValidarLicencia.setOnClickListener {
            // La vista solo debe hacer UNA cosa: delegar al presenter.
            val licencia = etLicencia.text.toString()
            presenter.onBotonValidarLicenciaPulsado(licencia)
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

    // ... (dentro de InfraccionesActivity.kt)

    // En InfraccionesActivity.kt

    override fun mostrarDatosVehiculo(vehiculo: VehiculoDTO) {
        tvMarca.text = vehiculo.marca ?: "N/D"
        tvModelo.text = vehiculo.modelo ?: "N/D"
        tvColor.text = vehiculo.color ?: "N/D"
        tvTipo.text = vehiculo.tipo ?: "N/D"

        mostrarMensaje("Datos del vehiculo encontrados.")
    }

    override fun mostrarDatosLicencia(licencia: LicenciaDTO) {
        runOnUiThread {
            tvNombreConductor.text =
                "${licencia.nombre ?: ""} ${licencia.apellido ?: ""}".trim()

            tvEstatusLicencia.text =
                "Estado: ${licencia.estado_licencia ?: "Desconocido"}"

            mostrarMensaje("Licencia encontrada.")
        }
    }


    override fun mostrarErrorConsulta(mensaje: String) {
        // ✅ BUENA PRÁCTICA HACERLO TAMBIÉN PARA LOS ERRORES
        runOnUiThread {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }
    }

    override fun mostrarCatalogoInfracciones(infracciones: List<TipoInfraccionDTO>) {

        // 1. Extrae los nombres de la lista de objetos para mostrarlos en el Spinner
        val nombresInfracciones = infracciones.map { it.nombre ?: "Sin nombre" }

        // 2. ✅ ¡PASO CLAVE QUE FALTABA! Crea el adaptador con los nombres.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresInfracciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 3. ✅ ¡PASO CLAVE QUE FALTABA! Asigna el adaptador al Spinner.
        spinnerInfracciones.adapter = adapter

        // 4. Ahora sí, configura el listener.
        spinnerInfracciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val infraccionSeleccionada = infracciones[position]

                // 1. Obtenemos la lista de artículos
                val listaDeArticulos = infraccionSeleccionada.articulo?.data

                // 2. Verificamos si la lista no es nula y no está vacía
                if (!listaDeArticulos.isNullOrEmpty()) {
                    // 3. Usamos StringBuilder para construir eficientemente el texto
                    val textoCompleto = StringBuilder()

                    // 4. Recorremos cada artículo en la lista
                    listaDeArticulos.forEachIndexed { index, articulo ->
                        textoCompleto.append(
                            """
                        Ordenamiento: ${articulo.ordenamiento ?: "N/A"}
                        Artículo: ${articulo.articulo_numero ?: "N/A"}
                        Contenido:
                        ${articulo.contenido ?: "No hay contenido."}
                        """.trimIndent()
                        )
                        // Añadimos un separador si no es el último artículo
                        if (index < listaDeArticulos.lastIndex) {
                            textoCompleto.append("\n\n---\n\n")
                        }
                    }

                    // 5. Asignamos el texto construido al TextView
                    tvArticuloInfraccion.text = textoCompleto.toString()

                } else {
                    // Mensaje por defecto si no hay artículos asociados
                    tvArticuloInfraccion.text = "No hay artículos específicos asociados a esta infracción."
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                tvArticuloInfraccion.text = "Seleccione una infracción para ver el artículo."
            }
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