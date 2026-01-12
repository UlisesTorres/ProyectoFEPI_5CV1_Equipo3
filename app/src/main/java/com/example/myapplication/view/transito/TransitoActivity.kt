package com.example.myapplication.view.transito

import TransitoPresenter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.configuracion.ConfiguracionActivity

class TransitoActivity : ComponentActivity(), TransitoContract.View {

    private lateinit var presenter: TransitoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transito_principal)

        presenter = TransitoPresenter(this)

        // Configuración de botones llamando al presenter
        findViewById<Button>(R.id.btnGenerarInfraccion).setOnClickListener {
            presenter.clickGenerarInfraccion()
        }
        findViewById<Button>(R.id.btnConfiguracion).setOnClickListener {
            presenter.clickConfiguracion()
        }
        findViewById<Button>(R.id.btnGenerarArrastre).setOnClickListener {
            presenter.clickGenerarArrastre()
        }
        findViewById<Button>(R.id.btnInfraccionRegistrada).setOnClickListener {
            presenter.clickHistorial()
        }
    }

    // Implementación de la navegación
    override fun navegarAGenerarInfraccion() {
        startActivity(Intent(this, InfraccionesActivity::class.java))
    }

    override fun navegarAConfiguracion() {
        startActivity(Intent(this, ConfiguracionActivity::class.java))
    }

    override fun navegarAGenerarArrastre() {
        startActivity(Intent(this, SeleccionarInfraccionActivity::class.java))
    }

    override fun navegarAHistorial() {
        startActivity(Intent(this, HistorialActivity::class.java))
    }
}