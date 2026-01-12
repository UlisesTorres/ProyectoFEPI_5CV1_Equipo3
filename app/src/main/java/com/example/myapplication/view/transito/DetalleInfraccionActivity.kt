package com.example.myapplication.view.transito

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class DetalleInfraccionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_infraccion)

        // Recibir los datos del Intent
        val folio = intent.getStringExtra("EXTRA_FOLIO") ?: "No disponible"
        val placa = intent.getStringExtra("EXTRA_PLACA") ?: "No disponible"
        val fecha = intent.getStringExtra("EXTRA_FECHA") ?: "No disponible"

        // Vincular vistas
        val tvFolio: TextView = findViewById(R.id.tv_detalle_folio)
        val tvPlaca: TextView = findViewById(R.id.tv_detalle_placa)
        val tvFecha: TextView = findViewById(R.id.tv_detalle_fecha)

        // Mostrar datos
        tvFolio.text = "Folio: $folio"
        tvPlaca.text = "Placa: $placa"
        // Formateamos un poco la fecha para que no muestre la hora
        tvFecha.text = "Fecha: ${fecha.substringBefore('T')}"
    }
}
    