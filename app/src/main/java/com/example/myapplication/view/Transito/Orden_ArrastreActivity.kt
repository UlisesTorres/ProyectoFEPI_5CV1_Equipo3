package com.example.myapplication.view.Transito

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R

class Orden_ArrastreActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí se hace la unión:
        setContentView(R.layout.activity_orden_arrastre)
    }
}