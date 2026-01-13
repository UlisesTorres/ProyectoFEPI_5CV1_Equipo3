package com.example.myapplication.view.operador_grua

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R

class DetalleSolicitudActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_solicitud)

        val idArrastre = intent.getIntExtra("id_arrastre", -1)
        val observaciones = intent.getStringExtra("observaciones")

        val tvFolio: TextView = findViewById(R.id.tv_detalle_folio)
        val tvObservaciones: TextView = findViewById(R.id.tv_detalle_placa) // Assuming tv_detalle_placa is for observaciones

        tvFolio.text = "ID Arrastre: $idArrastre"
        tvObservaciones.text = "Observaciones: $observaciones"

        val btnAceptar: Button = findViewById(R.id.btn_aceptar_solicitud)
        btnAceptar.setOnClickListener {
            // Aquí irá la lógica para aceptar la solicitud
            Toast.makeText(this, "Solicitud aceptada (ID: $idArrastre)", Toast.LENGTH_SHORT).show()
        }
    }
}
