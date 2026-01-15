package com.example.myapplication.view.operador_grua

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.operador_grua.OperadorGruaPresenter
import com.example.myapplication.view.configuracion.ConfiguracionActivity
import com.example.myapplication.view.corralones.InventarioActivity

class Operador_GruaActivity : ComponentActivity(), OperadorGruaContract.View {

    private lateinit var presenter: OperadorGruaContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operador_grua_principal)

        presenter = OperadorGruaPresenter(this)

        // Asignación de botones y eventos
        findViewById<Button>(R.id.btnArrastreActivo).setOnClickListener {
            presenter.clickArrastre()
        }

        findViewById<Button>(R.id.btnVerPeticiones).setOnClickListener {
            presenter.clickSolicitudes()
        }

        findViewById<Button>(R.id.btnHistorialGrua).setOnClickListener {
            presenter.clickHistorial()
        }

        // --- Botón NUEVO: Inventario ---
        findViewById<Button>(R.id.btnInventarioGrua).setOnClickListener {
            startActivity(Intent(this, InventarioActivity::class.java))
        }

        findViewById<Button>(R.id.btnConfiguracion).setOnClickListener {
            presenter.clickConfiguracion()
        }
    }

    // --- Implementación de navegación ---

    override fun navegarAArrastreEnCurso() {
        startActivity(Intent(this, Arrastre_En_CursoActivity::class.java))
    }

    override fun navegarASolicitudes() {
        startActivity(Intent(this, Solicitud_ArrastreActivity::class.java))
    }

    override fun navegarAHistorial() {
        startActivity(Intent(this, Historial_ArrastresActivity::class.java))
    }

    override fun navegarAConfiguracion() {
        startActivity(Intent(this, ConfiguracionActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
