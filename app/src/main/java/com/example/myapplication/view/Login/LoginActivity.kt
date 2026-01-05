package com.example.myapplication.view.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.Login.LoginPresenter
import com.example.myapplication.view.Transito.TransitoActivity
import com.example.myapplication.view.operador_grua.Operador_GruaActivity
import com.example.myapplication.view.supervisor.SupervisorActivity

class LoginActivity : ComponentActivity(), MainContract.View {
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

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
        startActivity(Intent(this, Operador_GruaActivity::class.java))
    }

    override fun navegarASupervisor() {
        Toast.makeText(this, "Entrando como Supervisor", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SupervisorActivity::class.java))
    }

    override fun navegarAGestor() {
        Toast.makeText(this, "Entrando como Gestor de Corralon", Toast.LENGTH_SHORT).show()
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}