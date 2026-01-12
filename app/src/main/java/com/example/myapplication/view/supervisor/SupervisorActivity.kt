package com.example.myapplication.view.supervisor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.supervisor.SupervisorPresenter
import com.example.myapplication.view.configuracion.ConfiguracionActivity

/**
 * Esta es la pantalla principal para el rol de Supervisor.
 * Desde aquí, puede navegar a las diferentes funcionalidades.
 */
class SupervisorActivity : ComponentActivity(), SupervisorContract.View  {

    private lateinit var presenter: SupervisorContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor_principal)
        presenter = SupervisorPresenter(this)
        // 1. Vincular los botones del layout con variables en el código
        //    Asegúrate de que los IDs ('R.id...') coincidan con los de tu archivo XML.
        val btnVerInfracciones: Button = findViewById(R.id.btnVerInfracciones) // Reemplaza con tu ID real
        val btnValidarInfracciones: Button = findViewById(R.id.btnValidarInfraccion) // Reemplaza con tu ID real

        // 2. Configurar el listener para el botón "Ver Infracciones"
        btnVerInfracciones.setOnClickListener {
            // Creamos un Intent para abrir la SupervisorHistorialActivity que ya preparamos
            val intent = Intent(this, SupervisorHistorialActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnConfiguracion).setOnClickListener {
            presenter.clickConfiguracion()
        }

        // 3. Configurar el listener para el botón "Validar Infracciones" (funcionalidad futura)
        btnValidarInfracciones.setOnClickListener {
            // Por ahora, podemos mostrar un mensaje temporal
            // hasta que implementes la nueva pantalla.
            // val intent = Intent(this, ValidarInfraccionesActivity::class.java)
            // startActivity(intent)

            // Mensaje temporal:
            android.widget.Toast.makeText(this, "Funcionalidad 'Validar Infracciones' en desarrollo", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun navegarAConfiguracion() {
        startActivity(Intent(this, ConfiguracionActivity::class.java))
    }
}
