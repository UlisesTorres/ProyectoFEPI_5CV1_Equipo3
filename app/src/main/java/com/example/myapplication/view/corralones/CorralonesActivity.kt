package com.example.myapplication.view.corralones

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.Login.LoginActivity
import com.example.myapplication.view.configuracion.ConfiguracionActivity


class CorralonesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corralones_principal)

        // 1. Asociar los botones del XML con variables de Kotlin
        val btnIngreso = findViewById<Button>(R.id.btnIngresoVehiculo)
        val btnInventario = findViewById<Button>(R.id.btnInventario)
        val btnSalida = findViewById<Button>(R.id.btnSalidaVehiculo)
        val btnHistorial = findViewById<Button>(R.id.btnHistorialMovimientos)
        val btnConfig = findViewById<Button>(R.id.btnConfiguracion)

        // 2. Configurar los eventos de clic (Listeners)

        btnIngreso.setOnClickListener {
            // Aquí navegarías a la pantalla de registro de ingreso
            startActivity(Intent(this, RegistrarIngresoActivity::class.java))
        }

        btnInventario.setOnClickListener {
            val intent = Intent(this, InventarioActivity::class.java)
            startActivity(intent)
        }

        btnSalida.setOnClickListener {
            val intent = Intent(this, LiberarActivity::class.java)
            startActivity(intent)
        }

        btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialCorralonActivity::class.java)
            startActivity(intent)
        }

        btnConfig.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }
    }
}