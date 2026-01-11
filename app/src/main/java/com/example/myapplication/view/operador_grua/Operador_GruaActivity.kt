package com.example.myapplication.view.operador_grua

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.configuracion.ConfiguracionActivity

class Operador_GruaActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_operador_grua_principal)

        val btnArrastre = findViewById<Button>(R.id.btnArrastreActivo)
        val btnSolicitud = findViewById<Button>(R.id.btnVerPeticiones)
        val btnHistorial = findViewById<Button>(R.id.btnHistorialGrua)
        val btnConfigu = findViewById<Button>(R.id.btnConfiguracion)

        btnArrastre.setOnClickListener {
            startActivity(Intent(this, Arrastre_En_CursoActivity::class.java))
        }

        btnSolicitud.setOnClickListener {
            startActivity(Intent(this, Solicitud_ArrastreActivity::class.java))
        }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, Historial_ArrastresActivity::class.java))
        }
        btnConfigu.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }
    }
}