package com.example.myapplication.view.corralones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.corralones.VehiculoInventario

class InventarioAdapter(
    private var vehiculos: List<VehiculoInventario>,
    private val onItemClick: (VehiculoInventario) -> Unit
) : RecyclerView.Adapter<InventarioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFolio: TextView = view.findViewById(R.id.tvFolioArrastre)
        val tvEstatus: TextView = view.findViewById(R.id.tvEstatus)
        val tvPlaca: TextView = view.findViewById(R.id.tvPlacaVehiculo)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaIngreso)
        val tvUbicacion: TextView = view.findViewById(R.id.tvUbicacionInterna)
        val tvObservaciones: TextView = view.findViewById(R.id.tvObservaciones)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventario_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val v = vehiculos[position]
        holder.tvFolio.text = "Folio: ${v.folio}"
        holder.tvEstatus.text = v.estatus
        holder.tvPlaca.text = "Placa: ${v.placa}"
        holder.tvFecha.text = "Ingreso: ${v.fechaIngreso}"
        holder.tvUbicacion.text = "📍 ${v.ubicacion}"
        holder.tvObservaciones.text = "Obs: ${v.observaciones}"

        holder.itemView.setOnClickListener { onItemClick(v) }
    }

    override fun getItemCount() = vehiculos.size

    fun updateData(newVehiculos: List<VehiculoInventario>) {
        vehiculos = newVehiculos
        notifyDataSetChanged()
    }
}
