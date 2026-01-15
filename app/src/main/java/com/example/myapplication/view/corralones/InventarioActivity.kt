package com.example.myapplication.view.corralones

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.corralones.InventarioModel
import com.example.myapplication.model.corralones.VehiculoInventario
import com.example.myapplication.presenter.corralones.InventarioPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventarioActivity : ComponentActivity(), InventarioContract.View {

    private lateinit var presenter: InventarioContract.Presenter
    private lateinit var adapter: InventarioAdapter
    private lateinit var layoutVacio: LinearLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario_corralon)

        recyclerView = findViewById(R.id.rvAutosAlmacenados)
        layoutVacio = findViewById(R.id.layoutCorralonVacio)
        val fabRefresh = findViewById<FloatingActionButton>(R.id.fabRefreshInventario)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InventarioAdapter(emptyList()) { vehiculo ->
            presenter.alSeleccionarVehiculo(vehiculo.id)
        }
        recyclerView.adapter = adapter

        presenter = InventarioPresenter(this, InventarioModel())
        presenter.cargarInventario()

        fabRefresh.setOnClickListener {
            presenter.cargarInventario()
        }
    }

    override fun mostrarVehiculosEnInventario(vehiculos: List<VehiculoInventario>) {
        if (vehiculos.isEmpty()) {
            recyclerView.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            adapter.updateData(vehiculos)
        }
    }

    override fun mostrarCargando() {}
    override fun ocultarCargando() {}

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarADetalleVehiculo(idVehiculo: String) {
        // Implementar navegación al detalle si se requiere
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
