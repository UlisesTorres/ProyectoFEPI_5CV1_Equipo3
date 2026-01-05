package com.example.myapplication.view.corralones

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.Login.LoginActivity

class CorralonesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corralones_principal)

        // 1. Asociar los botones del XML con variables de Kotlin
        val btnIngreso = findViewById<Button>(R.id.btnIngresoVehiculo)
        val btnInventario = findViewById<Button>(R.id.btnInventario)
        val btnSalida = findViewById<Button>(R.id.btnSalidaVehiculo)
        val btnHistorial = findViewById<Button>(R.id.btnHistorialMovimientos)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // 2. Configurar los eventos de clic (Listeners)

        btnIngreso.setOnClickListener {
            // Aquí navegarías a la pantalla de registro de ingreso
            Toast.makeText(this, "Abriendo Registro de Ingreso", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegistrarIngresoActivity::class.java))
        }

        btnInventario.setOnClickListener {
            Toast.makeText(this, "Consultando Inventario", Toast.LENGTH_SHORT).show()
        }

        btnSalida.setOnClickListener {
            Toast.makeText(this, "Abriendo Liberaciones", Toast.LENGTH_SHORT).show()
        }

        btnHistorial.setOnClickListener {
            Toast.makeText(this, "Abriendo Historial", Toast.LENGTH_SHORT).show()
        }

        btnCerrarSesion.setOnClickListener {
            procederCerrarSesion()
        }
    }

    private fun procederCerrarSesion() {
        // Lógica simple para regresar al Login
        val intent = Intent(this, LoginActivity::class.java)
        // Limpiar el historial de pantallas para que no pueda regresar con el botón atrás
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}