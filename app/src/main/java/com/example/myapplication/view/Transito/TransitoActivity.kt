package com.example.myapplication.view.Transito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.view.configuracion.ConfiguracionActivity

class TransitoActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transito_principal)

        val btnGenerarInf = findViewById<Button>(R.id.btnGenerarInfraccion)
        btnGenerarInf.setOnClickListener {
            val intent = Intent(this, InfraccionesActivity::class.java)
            startActivity(intent)
        }


        val btnConfig = findViewById<Button>(R.id.btnConfiguracion)
        btnConfig.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        val btnGenerarArrastre = findViewById<Button>(R.id.btnGenerarArrastre)
        btnGenerarArrastre.setOnClickListener {
            val intent = Intent(this, Orden_ArrastreActivity::class.java)
            startActivity(intent)
        }

        val btnInfracion = findViewById<Button>(R.id.btnInfraccionRegistrada)
        btnInfracion.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }


    }
}