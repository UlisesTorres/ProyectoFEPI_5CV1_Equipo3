package com.example.myapplication.view.transito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.infracciones.InfraccionesActivity

class TransitoActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transito_principal)

        val btnGenerarInf = findViewById<Button>(R.id.btnGenerarInfraccion)
        btnGenerarInf.setOnClickListener {
            val intent = Intent(this, InfraccionesActivity::class.java)
            startActivity(intent)
        }

    }
}