package com.example.myapplication.view.configuracion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.presenter.configuracion.ConfiguracionPresenter
import com.example.myapplication.view.login.LoginActivity

class ConfiguracionActivity: AppCompatActivity(), ConfiguracionContract.View{
    private lateinit var presenter: ConfiguracionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        presenter = ConfiguracionPresenter(this)

        val btnSesion = findViewById<Button>(R.id.btnCerrarSesion)

        btnSesion.setOnClickListener { presenter.clickCerrar() }
    }

    override fun navegarAlLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun mostrarConfirmacionCierre() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estas seguro que deseas salir?")
        builder.setPositiveButton("Si, Salir"){dialog, _ ->this@ConfiguracionActivity.presenter.confirmarCierreSesion()}
        builder.setNegativeButton("Cancelar",null)
        val dialog = builder.create()
        dialog.show()
    }
}