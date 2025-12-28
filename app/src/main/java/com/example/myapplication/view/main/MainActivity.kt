package com.example.myapplication.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.view.transito.TransitoActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIS = findViewById<Button>(R.id.btnIniciarSesion)
        btnIS.setOnClickListener {
            val intent = Intent(this, TransitoActivity::class.java)
            startActivity(intent)
        }
    }
}