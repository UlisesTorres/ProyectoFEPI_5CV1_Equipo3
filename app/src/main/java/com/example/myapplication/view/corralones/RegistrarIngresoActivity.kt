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
    private lateinit var etFolio: TextInputEditText
    private lateinit var etPlaca: TextInputEditText
    private lateinit var etNombreCorralon: TextInputEditText
    private lateinit var etDireccionCorralon: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_corralon)

        presenter = RegistrarIngresoPresenter(this, RegistrarIngresoModel())

        etFolio = findViewById(R.id.etFolioArrastre)
        etPlaca = findViewById(R.id.etPlaca)
        etNombreCorralon = findViewById(R.id.etNombreCorralon)
        etDireccionCorralon = findViewById(R.id.etDireccionCorralon)
        
        val btnBuscar = findViewById<MaterialButton>(R.id.btnBuscarFolio)
        btnRegistrar = findViewById(R.id.btnGuardarRegistro)

        btnBuscar.setOnClickListener {
            presenter.buscarFolio(etFolio.text.toString())
        }

        btnRegistrar.setOnClickListener {
            val nombre = etNombreCorralon.text.toString()
            val direccion = etDireccionCorralon.text.toString()
            presenter.registrarIngreso(nombre, direccion)
        }
    }

    override fun llenarDatosVehiculo(placa: String) {
        etPlaca.setText(placa)
        Toast.makeText(this, "Vehículo identificado", Toast.LENGTH_SHORT).show()
    }

    override fun habilitarRegistro(habilitar: Boolean) {
        btnRegistrar.isEnabled = habilitar
    }

    override fun limpiarFormulario() {
        etFolio.text?.clear()
        etPlaca.text?.clear()
        etNombreCorralon.text?.clear()
        etDireccionCorralon.text?.clear()
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun mostrarCargando() {}
    override fun ocultarCargando() {}

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
