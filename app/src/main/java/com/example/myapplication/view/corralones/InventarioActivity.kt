package com.example.myapplication.view.corralones

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.model.corralones.InventarioModel
import com.example.myapplication.presenter.corralones.InventarioPresenter

class InventarioActivity : ComponentActivity(), InventarioContract.View {

    private lateinit var presenter: InventarioContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario_corralon)

        presenter = InventarioPresenter(this, InventarioModel())

        // Cargar inventario al abrir la pantalla
        presenter.cargarInventario()
    }

    override fun mostrarVehiculosEnInventario(vehiculos: List<String>) {
        // Aquí pasas la lista a tu RecyclerView.Adapter
    }

    override fun mostrarCargando() { /* Mostrar ProgressBar */ }

    override fun ocultarCargando() { /* Ocultar ProgressBar */ }

    override fun mostrarError(mensaje: String) {
        // Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarADetalleVehiculo(idVehiculo: String) {
        // Abrir pantalla con fotos y detalles del auto almacenado
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}