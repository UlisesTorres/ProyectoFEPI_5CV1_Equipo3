package com.example.myapplication.view.transito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.transito.InfraccionData

class HistorialAdapter(
    private val infracciones: List<InfraccionData>,
    private val onItemClick: (InfraccionData) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.InfraccionViewHolder>() {

    inner class InfraccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFolio: TextView = itemView.findViewById(R.id.tvFolio)
        private val tvPlaca: TextView = itemView.findViewById(R.id.tvPlaca)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacion)

        fun bind(data: InfraccionData) {
            tvFolio.text = data.folio ?: "S/F"
            tvPlaca.text = "Placa: ${data.placa_vehiculo ?: "S/P"}"
            tvFecha.text = formatarFecha(data.fecha_infraccion ?: "")
            tvUbicacion.text = "Ubicación: ${data.ubicacion_infraccion ?: "N/D"}"

            itemView.setOnClickListener {
                onItemClick(data)
            }
        }

        private fun formatarFecha(fechaJson: String): String {
            return try {
                if (fechaJson.length >= 10) fechaJson.substring(0, 10) else fechaJson
            } catch (e: Exception) {
                fechaJson
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfraccionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_infraccion_card, parent, false)
        return InfraccionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfraccionViewHolder, position: Int) {
        holder.bind(infracciones[position])
    }

    override fun getItemCount(): Int = infracciones.size
}
