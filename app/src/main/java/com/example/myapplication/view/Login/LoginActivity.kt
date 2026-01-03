package com.example.myapplication.view.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.main.MainPresenter
import com.example.myapplication.view.transito.TransitoActivity

class LoginActivity : ComponentActivity(), MainContract.View {
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        val btnIS = findViewById<Button>(R.id.btnIniciarSesion)
        val etUser = findViewById<EditText>(R.id.editTextText)
        val etPass = findViewById<EditText>(R.id.editTextTextPassword)

        btnIS.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()
            presenter.intentarLogin(user, pass)
        }
    }

    override fun navegarATransito() {
        startActivity(Intent(this, TransitoActivity::class.java))
    }

    override fun navegarAGruas() {
        Toast.makeText(this, "Entrando como Operador de Grúa", Toast.LENGTH_SHORT).show()
    }

    override fun navegarASupervisor() {
        Toast.makeText(this, "Entrando como Supervisor", Toast.LENGTH_SHORT).show()
    }

    override fun navegarAGestor() {
        Toast.makeText(this, "Entrando como Gestor", Toast.LENGTH_SHORT).show()
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}