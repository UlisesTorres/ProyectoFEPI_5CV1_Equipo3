package com.example.myapplication.view.corralones

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.corralones.LiberarModel
import com.example.myapplication.model.corralones.VehiculoInventario
import com.example.myapplication.presenter.corralones.LiberarPresenter

class LiberarActivity : ComponentActivity(), LiberarContract.View {

    private lateinit var presenter: LiberarContract.Presenter
    private lateinit var adapter: LiberarAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liberar_auto)

        progressBar = findViewById(R.id.pbLiberar)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLiberarVehiculos)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Corregido: Pasamos documentId en lugar de id numérico
        adapter = LiberarAdapter(mutableListOf()) { vehiculo ->
            presenter.liberarVehiculo(vehiculo.documentId)
        }
        recyclerView.adapter = adapter

        presenter = LiberarPresenter(this, LiberarModel())
        presenter.cargarVehiculos()
    }

    override fun mostrarVehiculosParaLiberar(vehiculos: List<VehiculoInventario>) {
        adapter.updateData(vehiculos)
    }

    override fun removerVehiculoDeLista(documentId: String) {
        adapter.removeVehiculo(documentId)
        Toast.makeText(this, "Vehículo liberado correctamente", Toast.LENGTH_SHORT).show()
    }

    override fun mostrarCargando() {
        progressBar.visibility = View.VISIBLE
    }

    override fun ocultarCargando() {
        progressBar.visibility = View.GONE
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
