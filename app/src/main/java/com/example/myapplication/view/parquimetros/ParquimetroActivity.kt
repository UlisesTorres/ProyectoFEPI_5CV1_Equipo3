package com.example.myapplication.view.parquimetros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.configuracion.ConfiguracionActivity


class ParquimetroActivity: ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parquimetros_principal)

        val btnConsulta = findViewById<Button>(R.id.btnConsultarPlaca)
        btnConsulta.setOnClickListener {
            val intent = Intent(this, EstatusActivity::class.java)
            startActivity(intent)
        }

        val btnConfiguracion = findViewById<Button>(R.id.btnConfiguracion)
        btnConfiguracion.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }



    }
}