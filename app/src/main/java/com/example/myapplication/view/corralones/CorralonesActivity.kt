package com.example.myapplication.view.corralones

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.corralones.CorralonPresenter
import com.example.myapplication.view.configuracion.ConfiguracionActivity


class CorralonesActivity : ComponentActivity(), CorralonContract.View {

    private lateinit var presenter: CorralonContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corralones_principal)

        presenter = CorralonPresenter(this)

        // Inicializar botones y asignar eventos al presenter
        findViewById<Button>(R.id.btnIngresoVehiculo).setOnClickListener { presenter.alClickIngreso() }
        findViewById<Button>(R.id.btnInventario).setOnClickListener { presenter.alClickInventario() }
        findViewById<Button>(R.id.btnSalidaVehiculo).setOnClickListener { presenter.alClickSalida() }
        findViewById<Button>(R.id.btnConfiguracion).setOnClickListener { presenter.alClickConfiguracion() }
    }

    // --- Implementación de navegación ---

    override fun navegarAIngreso() {
        startActivity(Intent(this, RegistrarIngresoActivity::class.java))
    }

    override fun navegarAInventario() {
        startActivity(Intent(this, InventarioActivity::class.java))
    }

    override fun navegarALiberacion() {
        startActivity(Intent(this, LiberarActivity::class.java))
    }

    override fun navegarAHistorial() {
        // Esta función se mantiene por contrato pero ya no se dispara desde el botón eliminado
    }


    override fun navegarAConfiguracion() {
        startActivity(Intent(this, ConfiguracionActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
