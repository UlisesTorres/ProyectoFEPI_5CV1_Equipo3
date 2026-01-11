package com.example.myapplication.view.parquimetros

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.parquimetros.EstatusModel
import com.example.myapplication.presenter.parquimetro.EstatusPresenter


class EstatusActivity : ComponentActivity(), EstatusContract.View {

    private lateinit var presenter: EstatusContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatus)

        // Inyectamos el modelo al presenter
        presenter = EstatusPresenter(this, EstatusModel())

        // Supongamos que recibes la placa de la pantalla anterior o de un EditText
        val placaEjemplo = intent.getStringExtra("placa") ?: ""

        if (placaEjemplo.isNotEmpty()) {
            presenter.consultarPlaca(placaEjemplo)
        }
    }

    override fun mostrarCargando() {
        // Mostrar un ProgressBar
    }

    override fun ocultarCargando() {
        // Ocultar ProgressBar
    }

    override fun mostrarEstatus(mensaje: String, esValido: Boolean) {
        // Pintar el texto en un TextView y quizás poner el fondo verde/rojo
    }

    override fun mostrarError(error: String) {
        // Mostrar un Toast o un diálogo de error
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}