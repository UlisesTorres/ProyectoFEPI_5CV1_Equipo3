package com.example.myapplication.view.corralones

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegistrarIngresoActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_corralon)

        val btnBuscar = findViewById<MaterialButton>(R.id.btnBuscarFolio)
        val etPlaca = findViewById<TextInputEditText>(R.id.etPlaca)
        val etFolio = findViewById<TextInputEditText>(R.id.etFolioInfraccion)

        btnBuscar.setOnClickListener {
            val folio = etFolio.text.toString()
            if (folio.isNotEmpty()) {
                // Aquí iría tu lógica de búsqueda (Base de datos o API)
                Toast.makeText(this, "Buscando datos del folio: $folio", Toast.LENGTH_SHORT).show()

                // Ejemplo de llenado automático:
                // etPlaca.setText("ABC-123")
            } else {
                etFolio.error = "Ingresa un folio para buscar"
            }
        }

    }


}