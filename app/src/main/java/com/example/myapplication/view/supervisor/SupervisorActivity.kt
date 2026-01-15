package com.example.myapplication.view.supervisor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.supervisor.SupervisorPresenter
import com.example.myapplication.view.configuracion.ConfiguracionActivity
import com.example.myapplication.view.transito.HistorialActivity

class SupervisorActivity : ComponentActivity(), SupervisorContract.View  {

    private lateinit var presenter: SupervisorContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor_principal)
        presenter = SupervisorPresenter(this)

        val btnValidar: Button = findViewById(R.id.btnVerInfracciones) // Antes era ver, ahora es validar
        val btnVerHistorial: Button = findViewById(R.id.btnValidarInfraccion) // Antes era validar, ahora es historial

        // 1. Botón VALIDAR (El que abre la lista con CLIC habilitado)
        btnValidar.setOnClickListener {
            val intent = Intent(this, SupervisorHistorialActivity::class.java)
            startActivity(intent)
        }

        // 2. Botón VER HISTORIAL (El que abre la lista SIN CLIC habilitado)
        btnVerHistorial.setOnClickListener {
            // Usaremos HistorialActivity pero sin permitir navegación al detalle más adelante
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnConfiguracion).setOnClickListener {
            presenter.clickConfiguracion()
        }
    }

    override fun navegarAConfiguracion() {
        startActivity(Intent(this, ConfiguracionActivity::class.java))
    }
}
