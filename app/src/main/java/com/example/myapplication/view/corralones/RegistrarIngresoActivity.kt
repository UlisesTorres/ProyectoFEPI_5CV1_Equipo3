package com.example.myapplication.view.corralones

import android.os.Bundle
import android.widget.Button
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
    private lateinit var btnRegistrar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_corralon)

        presenter = RegistrarIngresoPresenter(this, RegistrarIngresoModel())

        val btnBuscar = findViewById<MaterialButton>(R.id.btnBuscarFolio)
        etPlaca = findViewById(R.id.etPlaca)
        etFolio = findViewById(R.id.etFolioInfraccion)
        // Asumiendo que tienes un botón de guardado en el XML
        // btnRegistrar = findViewById(R.id.btnGuardarIngreso)

        btnBuscar.setOnClickListener {
            presenter.buscarFolio(etFolio.text.toString())
        }
    }

    override fun llenarDatosVehiculo(placa: String) {
        etPlaca.setText(placa)
        Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show()
    }

    override fun mostrarCargando() {
        // Mostrar ProgressDialog o ProgressBar
    }

    override fun ocultarCargando() {
        // Ocultar ProgressDialog
    }

    override fun mostrarError(mensaje: String) {
        etFolio.error = mensaje
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun habilitarRegistro(habilitar: Boolean) {
        // btnRegistrar.isEnabled = habilitar
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}