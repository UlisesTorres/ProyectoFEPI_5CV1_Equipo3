package com.example.myapplication.view.parquimetros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.parquimetro.ParquimetroPresenter
import com.example.myapplication.view.configuracion.ConfiguracionActivity


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
    }

    override fun navegarAStatus() {
        val intent = Intent(this, EstatusActivity::class.java)
        startActivity(intent)
    }

    override fun navegarAConfiguracion() {
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    override fun mostrarMensajeError(mensaje: String) {
        // Aquí podrías mostrar un Toast o un Dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}