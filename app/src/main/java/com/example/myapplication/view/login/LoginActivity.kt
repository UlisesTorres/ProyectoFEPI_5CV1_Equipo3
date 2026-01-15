package com.example.myapplication.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.login.LoginPresenter
import com.example.myapplication.view.corralones.CorralonesActivity
import com.example.myapplication.view.operador_grua.Operador_GruaActivity
import com.example.myapplication.view.supervisor.SupervisorActivity
import com.example.myapplication.view.transito.TransitoActivity


class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

        // Referencias a la interfaz
        val btnIS = findViewById<Button>(R.id.btnIniciarSesion)
        val etUser = findViewById<EditText>(R.id.etUsuario)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnRP = findViewById<Button>(R.id.btnRecuperarPassword)



        btnIS.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                presenter.intentarLogin(user, pass)
            } else {
                mostrarError("Por favor, llena todos los campos")
            }
        }

       btnRP.setOnClickListener {
            presenter.clickRecuperarPassword()
        }
    }

    override fun loginExitoso(
        token: String,
        userId: Int,
        roleName: String,
        username: String,
        email: String
    ) {
        val prefs = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("jwt_token", token)
            putInt("user_id", userId)
            putString("role", roleName)
            putString("username", username)
            putString("email", email)
            apply()
        }
    }


    override fun navegarATransito() {
        startActivity(Intent(this, TransitoActivity::class.java))
    }

    override fun navegarAGruas() {
        startActivity(Intent(this, Operador_GruaActivity::class.java))
    }

    override fun navegarASupervisor() {
        startActivity(Intent(this, SupervisorActivity::class.java))
    }

    override fun navegarAGestor() {
        startActivity(Intent(this, CorralonesActivity::class.java))
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun mostrarAlertaRecuperar() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Recuperar contraseña")
            .setMessage("Para restablecer su contraseña, por favor contacte al administrador del sistema.")
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}