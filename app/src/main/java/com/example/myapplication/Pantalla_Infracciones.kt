package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class Pantalla_Infracciones : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infracciones)


        val spinner =findViewById<Spinner>(R.id.spinnerInfracciones)

        val infracciones = listOf(
            "Exceso de velocidad",
            "Estacionarse en lugar prohibido",
            "No respetar semáforo",
            "Uso de celular al conducir"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            infracciones
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter = adapter
    }
}

