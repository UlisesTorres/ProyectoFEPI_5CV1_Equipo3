package com.example.myapplication.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.presenter.Login.LoginPresenter
import com.example.myapplication.view.corralones.CorralonesActivity
import com.example.myapplication.view.operador_grua.Operador_GruaActivity
import com.example.myapplication.view.supervisor.SupervisorActivity
import com.example.myapplication.view.transito.TransitoActivity
import com.example.myapplication.view.parquimetros.ParquimetroActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

        val servicio = RetrofitClient.apiService

        servicio.testConexion().enqueue(object : Callback<okhttp3.ResponseBody> {
            override fun onResponse(call: Call<okhttp3.ResponseBody>, response: Response<okhttp3.ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("API_EXITO", "¡Conectado! El servidor respondió")
                    Toast.makeText(this@LoginActivity, "Conexión exitosa a Railway", Toast.LENGTH_SHORT).show()
                } else {
                    // Código 403, revisa los permisos en Strapi
                    Log.e("API_ERROR", "Error del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {
                // revisa URL en RetrofitClient o internet
                Log.e("API_FALLO", "Fallo total: ${t.message}")
            }
        })

        // Referencias a la interfaz
        val btnIS = findViewById<Button>(R.id.btnIniciarSesion)
        val etUser = findViewById<EditText>(R.id.etUsuario)
        val etPass = findViewById<EditText>(R.id.etPassword)

        btnIS.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                presenter.intentarLogin(user, pass)
            } else {
                mostrarError("Por favor, llena todos los campos")
            }
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

    override fun navegarAParquimetro(){
        startActivity(Intent(this, ParquimetroActivity::class.java))
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}