package com.example.myapplication.view.Transito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.example.myapplication.R
import com.example.myapplication.view.Login.LoginActivity

class TransitoActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transito_principal)

        val btnGenerarInf = findViewById<Button>(R.id.btnGenerarInfraccion)
        btnGenerarInf.setOnClickListener {
            val intent = Intent(this, InfraccionesActivity::class.java)
            startActivity(intent)
        }


        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas salir?")
                .setPositiveButton("Cerrar Sesión") { _, _ ->
                    // Llamamos a la función para limpiar y salir
                    procederCerrarSesion()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        val btnGenerarArrastre = findViewById<Button>(R.id.btnGenerarArrastre)
        btnGenerarArrastre.setOnClickListener {
            //val intent = Intent(this, Orden_ArrastreActivity::class.java)
            //startActivity(intent)
        }


    }

    private fun procederCerrarSesion() {
        val prefs = getSharedPreferences("SesionOficial", MODE_PRIVATE)
        prefs.edit { clear() }

        // FLAG_ACTIVITY_CLEAR_TASK limpia el historial para que no puedan volver atrás
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

}