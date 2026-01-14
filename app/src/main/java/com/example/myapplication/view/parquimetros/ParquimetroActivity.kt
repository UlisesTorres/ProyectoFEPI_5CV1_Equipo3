package com.example.myapplication.view.parquimetros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast // Añadido para usarlo en mostrarMensajeError
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.parquimetro.ParquimetroPresenter
import com.example.myapplication.view.configuracion.ConfiguracionActivity
import com.example.myapplication.view.transito.EstatusActivity

class ParquimetroActivity : ComponentActivity(), ParquimetroContract.View {

    private lateinit var presenter: ParquimetroContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parquimetros_principal)

        // Inicializamos el presenter
        presenter = ParquimetroPresenter(this)

        val btnConsulta = findViewById<Button>(R.id.btnConsultarPlaca)
        btnConsulta.setOnClickListener {
            presenter.alHacerClickConsultar()
        }

        val btnConfiguracion = findViewById<Button>(R.id.btnConfiguracion)
        btnConfiguracion.setOnClickListener {
            presenter.alHacerClickConfiguracion()
        }

        // --- CORRECCIÓN DE SINTAXIS ---
        val btnReporte = findViewById<Button>(R.id.btnReportarInfraccion) // Se añadió el paréntesis
        btnReporte.setOnClickListener {
            presenter.alHacerClickReporte()
        }
    }

    override fun navegarAStatus() {
        val intent = Intent(this, EstatusActivity::class.java)
        startActivity(intent)
    }

    override fun navegarAConfiguracion() {
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    // En ParquimetroActivity.kt

    override fun navegarAReporte() {
        // --- CORRECCIÓN ---
        // Cambiamos ReporteActivity::class.java por la clase que realmente existe
        // y que contiene la información de contacto.
        val intent = Intent(this, ReporteActivity::class.java)
        startActivity(intent)
    }


    override fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
