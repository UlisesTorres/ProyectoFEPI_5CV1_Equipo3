package com.example.myapplication.view.supervisor

import com.example.myapplication.view.transito.HistorialAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.transito.HistorialInfraccionesModel
import com.example.myapplication.model.transito.InfraccionData
import com.example.myapplication.presenter.transito.HistorialInfraccionesPresenter
import com.example.myapplication.view.supervisor.DetalleInfraccionActivity
import com.example.myapplication.view.transito.HistorialInfraccionesContract
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject

class SupervisorHistorialActivity : ComponentActivity(), HistorialInfraccionesContract.View {

    private lateinit var presenter: HistorialInfraccionesContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor_historial)

        recyclerView = findViewById(R.id.rvInfraccionesHistorial)
        layoutVacio = findViewById(R.id.layoutHistorialVacio)
        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter = HistorialInfraccionesPresenter(this, HistorialInfraccionesModel())

        findViewById<FloatingActionButton>(R.id.fabRefresh).setOnClickListener {
            presenter.obtenerInfraccionesDelOficial()
        }

        presenter.obtenerInfraccionesDelOficial()
    }

    override fun mostrarListaInfracciones(infracciones: List<InfraccionData>) {
        if (infracciones.isEmpty()) {
            recyclerView.visibility = View.GONE
            layoutVacio.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            layoutVacio.visibility = View.GONE
            recyclerView.adapter = HistorialAdapter(infracciones) { infraccion ->
                presenter.alSeleccionarInfraccion(infraccion)
            }
        }
    }

    override fun navegarADetalleInfraccion(infraccion: InfraccionData) {
        val intent = Intent(this, DetalleInfraccionActivity::class.java).apply {
            putExtra("EXTRA_ID", infraccion.id ?: -1)
            putExtra("EXTRA_FOLIO", infraccion.folio ?: "S/F")
            putExtra("EXTRA_PLACA", infraccion.placa_vehiculo ?: "S/P")
            putExtra("EXTRA_FECHA", infraccion.fecha_infraccion ?: "")
            putExtra("EXTRA_UBICACION", infraccion.ubicacion_infraccion ?: "N/D")
            
            val fotosUrls = mutableListOf<String>()
            infraccion.evidencia?.let { evidencia ->
                try {
                    val gson = Gson()
                    val root = gson.toJsonTree(evidencia).asJsonObject
                    val data = root.get("data")
                    if (!data.isJsonNull) {
                        val list = if (data.isJsonArray) data.asJsonArray else listOf(data.asJsonObject)
                        list.forEach { item ->
                            val itemObj = if (item is JsonObject) item else item.asJsonObject
                            fotosUrls.add(itemObj.get("attributes").asJsonObject.get("url").asString)
                        }
                    }
                } catch (e: Exception) {}
            }
            putStringArrayListExtra("EXTRA_FOTOS", ArrayList(fotosUrls))

            infraccion.firma?.let { firma ->
                try {
                    val gson = Gson()
                    val root = gson.toJsonTree(firma).asJsonObject
                    val data = root.get("data")
                    if (!data.isJsonNull) {
                        val dataObj = data.asJsonObject
                        putExtra("EXTRA_FIRMA", dataObj.get("attributes").asJsonObject.get("url").asString)
                    }
                } catch (e: Exception) {}
            }
        }
        startActivity(intent)
    }

    override fun mostrarCargando() {}
    override fun ocultarCargando() {}
    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destruir()
    }
}
