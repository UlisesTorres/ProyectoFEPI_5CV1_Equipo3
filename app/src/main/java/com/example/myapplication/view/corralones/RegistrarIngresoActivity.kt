package com.example.myapplication.view.corralones

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.corralones.RegistrarIngresoModel
import com.example.myapplication.presenter.corralones.RegistrarIngresoPresenter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegistrarIngresoActivity : ComponentActivity(), RegistrarIngresoContract.View {

    private lateinit var presenter: RegistrarIngresoContract.Presenter
    private lateinit var etPlaca: TextInputEditText
    private lateinit var etFolio: TextInputEditText
    private lateinit var etMarca: TextInputEditText
    private lateinit var etEstado: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_corralon)

        presenter = RegistrarIngresoPresenter(this, RegistrarIngresoModel())

        // Vincular con los nuevos IDs del XML
        val btnBuscar = findViewById<MaterialButton>(R.id.btnBuscarFolio)
        etFolio = findViewById(R.id.etFolioArrastre) // <-- CAMBIO AQUÍ
        etPlaca = findViewById(R.id.etPlaca)
        etMarca = findViewById(R.id.etMarcaModelo)
        etEstado = findViewById(R.id.etEstadoVehiculo)
        btnRegistrar = findViewById(R.id.btnGuardarRegistro) // <-- CAMBIO AQUÍ

        btnBuscar.setOnClickListener {
            val folio = etFolio.text.toString()
            if (folio.isNotEmpty()) {
                presenter.buscarFolio(folio)
            } else {
                etFolio.error = "Ingresa un folio"
            }
        }

        btnRegistrar.setOnClickListener {
            // Lógica para guardar el registro
            Toast.makeText(this, "Registro finalizado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun llenarDatosVehiculo(placa: String) {
        etPlaca.setText(placa)
        Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show()
    }

    override fun mostrarCargando() {}
    override fun ocultarCargando() {}

    override fun mostrarError(mensaje: String) {
        etFolio.error = mensaje
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun habilitarRegistro(habilitar: Boolean) {
        btnRegistrar.isEnabled = habilitar
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
