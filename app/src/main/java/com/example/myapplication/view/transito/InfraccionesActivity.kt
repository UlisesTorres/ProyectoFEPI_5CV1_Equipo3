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
import android.text.InputFilter
import android.text.InputType
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

    // --- NUEVAS VISTAS MANUALES ---
    private lateinit var cbVehiculoForaneo: CheckBox
    private lateinit var layoutVehiculoLectura: LinearLayout
    private lateinit var layoutVehiculoManual: LinearLayout
    private lateinit var spinnerTipoManual: Spinner

    private lateinit var spinnerMarcaManual: Spinner
    private lateinit var spinnerColorManual: Spinner
    private lateinit var spinnerModeloManual: Spinner


    private lateinit var cbSinLicencia: CheckBox
    private lateinit var layoutLicenciaLectura: LinearLayout
    private lateinit var layoutLicenciaManual: LinearLayout
    private lateinit var etNombreManual: EditText
    private lateinit var etApellidoManual: EditText

    // Card 3: Nueva vista de artículo
    private lateinit var tvArticuloInfraccion: TextView

    // Mapa
    private var mapLibreMap: MapLibreMap? = null
    private var marcadorInfraccion: Marker? = null

    private var placaValida = false
    private var licenciaValida = false

    private var tipoInfraccionSeleccionadoId: Int? = null
    private var articulosSeleccionadosIds: List<Int> = emptyList()

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

        // Inicializar Vistas Manuales - Vehículo
        cbVehiculoForaneo = findViewById(R.id.cbVehiculoForaneo)
        layoutVehiculoLectura = findViewById(R.id.layoutVehiculoLectura)
        layoutVehiculoManual = findViewById(R.id.layoutVehiculoManual)
        spinnerTipoManual = findViewById(R.id.spinnerTipoManual)
        spinnerMarcaManual = findViewById(R.id.spinnerMarcaManual)
        spinnerColorManual = findViewById(R.id.spinnerColorManual)
        spinnerModeloManual = findViewById(R.id.spinnerModeloManual)

        // Inicializar Vistas Manuales - Licencia
        cbSinLicencia = findViewById(R.id.cbSinLicencia)
        layoutLicenciaLectura = findViewById(R.id.layoutLicenciaLectura)
        layoutLicenciaManual = findViewById(R.id.layoutLicenciaManual)
        etNombreManual = findViewById(R.id.etNombreManual)
        etApellidoManual = findViewById(R.id.etApellidoManual)

        configurarSpinnerTipoManual() // Función para llenar el tipo de auto
        configurarListenersManuales() // Función para ocultar/mostrar




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

        etPlacas.filters = arrayOf(InputFilter.LengthFilter(7))
        etLicencia.filters = arrayOf(InputFilter.LengthFilter(10))
        etLicencia.inputType = InputType.TYPE_CLASS_NUMBER

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
            // 1. Validar sección de Vehículo
            if (cbVehiculoForaneo.isChecked) {
                // Validación Manual
                if (etPlacas.text.toString().trim().isEmpty()) {
                    mostrarMensaje("Ingrese la placa del vehículo foráneo.")
                    return@setOnClickListener
                }
                // Nota: Los Spinners siempre tienen una opción seleccionada por defecto,
                // pero puedes validar que no sea una opción tipo "Seleccione..." si la agregaste.
            } else {
                // Validación Automática
                if (!placaValida) {
                    mostrarMensaje("Debe consultar una placa válida antes de continuar.")
                    return@setOnClickListener
                }
            }

            // 2. Validar sección de Conductor
            if (cbSinLicencia.isChecked) {
                // Validación Manual: Nombre y Apellido obligatorios
                val nombre = etNombreManual.text.toString().trim()
                val apellido = etApellidoManual.text.toString().trim()

                if (nombre.isEmpty()) {
                    etNombreManual.error = "El nombre es obligatorio"
                    etNombreManual.requestFocus()
                    return@setOnClickListener
                }
                if (apellido.isEmpty()) {
                    etApellidoManual.error = "El apellido es obligatorio"
                    etApellidoManual.requestFocus()
                    return@setOnClickListener
                }
            } else {
                // Validación Automática
                if (!licenciaValida) {
                    if (etLicencia.text.toString().trim().isEmpty()) {
                        mostrarMensaje("Ingrese el número de licencia.")
                    } else {
                        mostrarMensaje("Debe validar la licencia antes de continuar.")
                    }
                    return@setOnClickListener
                }
            }


            val placas = etPlacas.text.toString().uppercase().trim()
            val tipoInfraccion = spinnerInfracciones.selectedItem.toString()
            val articuloInfraccion = tvArticuloInfraccion.text.toString()

            presenter.validarYGuardarInfraccion(placas, tipoInfraccion, articuloInfraccion)
        }

        btnMyLocation.setOnClickListener {
            mapLibreMap?.getStyle { style -> activarUbicacion(style) }
        }

        mapView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }


    }


    override fun mostrarDatosVehiculo(vehiculo: VehiculoDTO) {
        placaValida = true
        tvMarca.text = vehiculo.marca ?: "N/D"
        tvModelo.text = vehiculo.modelo ?: "N/D"
        tvColor.text = vehiculo.color ?: "N/D"
        tvTipo.text = vehiculo.tipo ?: "N/D"

        mostrarMensaje("Datos del vehiculo encontrados.")
    }

    override fun mostrarDatosLicencia(licencia: LicenciaDTO) {
        licenciaValida = true
        runOnUiThread {
            tvNombreConductor.text =
                "${licencia.nombre ?: ""} ${licencia.apellido ?: ""}".trim()

            tvEstatusLicencia.text =
                "Estado: ${licencia.estado_licencia ?: "Desconocido"}"

            mostrarMensaje("Licencia encontrada.")
        }
    }


    override fun mostrarErrorConsulta(mensaje: String) {
        placaValida = false
        licenciaValida = false
        // ✅ BUENA PRÁCTICA HACERLO TAMBIÉN PARA LOS ERRORES
        runOnUiThread {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }
    }

    override fun mostrarCatalogoInfracciones(infracciones: List<TipoInfraccionDTO>) {

        if (infracciones.isEmpty()) {
            deshabilitarSpinnerInfracciones()
            return
        }

        val nombresInfracciones = infracciones.map { it.nombre ?: "Sin nombre" }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresInfracciones
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerInfracciones.adapter = adapter
        spinnerInfracciones.isEnabled = true

        spinnerInfracciones.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val infraccionSeleccionada = infracciones[position]
                    val listaDeArticulos = infraccionSeleccionada.articulo_id // <- lista directa ahora
                    tipoInfraccionSeleccionadoId = infraccionSeleccionada.id

                    if (!listaDeArticulos.isNullOrEmpty()) {
                        val textoCompleto = StringBuilder()
                        listaDeArticulos.forEachIndexed { index, articulo ->
                            textoCompleto.append(
                                """
                            Ordenamiento: ${articulo.ordenamiento ?: "N/A"}
                            Artículo: ${articulo.articulo_numero ?: "N/A"}
                            Contenido:
                            ${articulo.contenido ?: "No hay contenido."}
                            Ambito: ${articulo.ambito ?: "N/A"}
                            Fecha publicación: ${articulo.fecha_publicacion ?: "N/A"}
                            Fecha última reforma: ${articulo.fecha_ultima_reforma ?: "N/A"}
                            """.trimIndent()
                            )
                            if (index < listaDeArticulos.lastIndex) {
                                textoCompleto.append("\n\n---\n\n")
                            }
                        }
                        tvArticuloInfraccion.text = textoCompleto.toString()
                        articulosSeleccionadosIds = listaDeArticulos.map { it.id }
                    } else {
                        tvArticuloInfraccion.text =
                            "No hay artículos específicos asociados a esta infracción."
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    tvArticuloInfraccion.text =
                        "Seleccione una infracción para ver el artículo."
                }
            }
    }



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

        // --- 1. DATOS BASE (Lo que ya tenías) ---
        intent.putExtra("PLACAS", placas)
        intent.putExtra("DIRECCION", direccion)
        intent.putExtra("FECHA", fechaISO)

        tipoInfraccionSeleccionadoId?.let {
            intent.putIntegerArrayListExtra("TIPO_INFRACCION_IDS", arrayListOf(it))
        }
        intent.putIntegerArrayListExtra("ARTICULOS_IDS", ArrayList(articulosSeleccionadosIds))

        // --- 2. LÓGICA DE VEHÍCULO (Automático vs Spinner) ---
        if (cbVehiculoForaneo.isChecked) {
            intent.putExtra("ES_FORANEO", true)
            intent.putExtra("VEHICULO_TIPO", spinnerTipoManual.selectedItem.toString())
            intent.putExtra("VEHICULO_MARCA", spinnerMarcaManual.selectedItem.toString())
            intent.putExtra("VEHICULO_MODELO", spinnerModeloManual.selectedItem.toString())
            intent.putExtra("VEHICULO_COLOR", spinnerColorManual.selectedItem.toString())
        } else {
            intent.putExtra("ES_FORANEO", false)
            intent.putExtra("VEHICULO_TIPO", tvTipo.text.toString())
            intent.putExtra("VEHICULO_MARCA", tvMarca.text.toString())
            intent.putExtra("VEHICULO_MODELO", tvModelo.text.toString())
            intent.putExtra("VEHICULO_COLOR", tvColor.text.toString())
        }

        // --- 3. LÓGICA DE CONDUCTOR (Automático vs Manual) ---
        if (cbSinLicencia.isChecked) {
            intent.putExtra("ES_MANUAL_CONDUCTOR", true)
            val nombreCompleto = "${etNombreManual.text.toString().trim()} ${etApellidoManual.text.toString().trim()}"
            intent.putExtra("CONDUCTOR_NOMBRE", nombreCompleto)
            intent.putExtra("NUM_LICENCIA", "NO PRESENTA")
        } else {
            intent.putExtra("ES_MANUAL_CONDUCTOR", false)
            intent.putExtra("CONDUCTOR_NOMBRE", tvNombreConductor.text.toString())
            intent.putExtra("NUM_LICENCIA", etLicencia.text.toString())
        }

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

    override fun deshabilitarSpinnerInfracciones() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("No disponible")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerInfracciones.adapter = adapter
        spinnerInfracciones.isEnabled = false
    }


    override fun ocultarTeclado() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    val datosVehiculos = mapOf(
        "Chevrolet" to listOf("Aveo", "Onix", "Captiva", "Silverado"),
        "Ford" to listOf("Figo", "Focus", "Explorer", "F-150"),
        "Nissan" to listOf("Versa", "Sentra", "March", "Frontier"),
        "Mazda" to listOf("Mazda 3", "CX-5", "MX-5"),
        "Audi" to listOf("A3", "A4", "Q5", "Q7"),
        "Dodge" to listOf("Attitude", "Journey", "Ram")
    )

    private fun configurarSpinnerTipoManual() {
        // 1. Llenar Marcas (El Spinner "Padre")
        val marcas = datosVehiculos.keys.toList()
        val adapterMarca = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMarcaManual.adapter = adapterMarca

        val listaTipos = listOf("Automovil", "Camioneta", "Suv")
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTipos)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoManual.adapter = adapterTipo

        // 2. Llenar Colores (Indiferente)
        val colores = listOf("Blanco", "Negro", "Gris", "Plata", "Rojo", "Azul")
        spinnerColorManual.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colores).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // 3. Lógica de Dependencia: Cuando cambia la Marca, actualiza el Modelo
        spinnerMarcaManual.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val marcaSeleccionada = marcas[position]
                val modelosCorrespondientes = datosVehiculos[marcaSeleccionada] ?: listOf("N/A")

                // Actualizamos el Spinner de Modelos con la lista filtrada
                val adapterModelo = ArrayAdapter(this@InfraccionesActivity, android.R.layout.simple_spinner_item, modelosCorrespondientes)
                adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerModeloManual.adapter = adapterModelo
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun configurarListenersManuales() {
        cbVehiculoForaneo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layoutVehiculoLectura.visibility = View.GONE
                layoutVehiculoManual.visibility = View.VISIBLE
                btnConsultarPlaca.visibility = View.GONE
                placaValida = true // Habilitamos el paso si es manual
            } else {
                layoutVehiculoLectura.visibility = View.VISIBLE
                layoutVehiculoManual.visibility = View.GONE
                btnConsultarPlaca.visibility = View.VISIBLE
                placaValida = false // Requiere consulta de nuevo
            }
        }

        cbSinLicencia.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layoutLicenciaLectura.visibility = View.GONE
                layoutLicenciaManual.visibility = View.VISIBLE
                btnValidarLicencia.visibility = View.GONE
                licenciaValida = true // Habilitamos el paso si es manual
            } else {

                layoutLicenciaLectura.visibility = View.VISIBLE
                layoutLicenciaManual.visibility = View.GONE
                btnValidarLicencia.visibility = View.VISIBLE
                licenciaValida = false // Requiere validación de nuevo
            }
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