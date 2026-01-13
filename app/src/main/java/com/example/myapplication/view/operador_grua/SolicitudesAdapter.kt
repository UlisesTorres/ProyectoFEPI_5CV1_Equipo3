package com.example.myapplication.view.operador_grua

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes

class SolicitudesAdapter(
    private var solicitudes: List<GenerarArrastreAttributes>,
    private val onItemClick: (GenerarArrastreAttributes) -> Unit
) : RecyclerView.Adapter<SolicitudesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFolio: TextView = view.findViewById(R.id.tvFolio)
        val tvPlaca: TextView = view.findViewById(R.id.tvPlaca)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_infraccion_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val solicitud = solicitudes[position]

        holder.tvFolio.text = "ID ARRASTRE: ${solicitud.id}"
        holder.tvPlaca.text = "Observaciones: ${solicitud.observaciones}"

        holder.tvFecha.visibility = View.GONE

        holder.itemView.setOnClickListener {
            onItemClick(solicitud)
        }
    }

    override fun getItemCount() = solicitudes.size

    fun updateData(newSolicitudes: List<GenerarArrastreAttributes>) {
        this.solicitudes = newSolicitudes
        notifyDataSetChanged()
    }
}
