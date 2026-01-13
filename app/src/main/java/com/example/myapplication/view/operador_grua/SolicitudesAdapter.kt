package com.example.myapplication.view.operador_grua

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes // <-- CAMBIA LA IMPORTACIÓN

class SolicitudesAdapter(
    private var solicitudes: List<GenerarArrastreAttributes>, // <-- CAMBIA EL TIPO DE LISTA
    private val onItemClick: (GenerarArrastreAttributes) -> Unit // <-- CAMBIA EL TIPO DEL LISTENER
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

        // --- INICIO DE LA CORRECCIÓN ---
        // Accedemos a los campos directamente desde el objeto 'solicitud'.
        holder.tvFolio.text = "FOLIO ARRASTRE: ${solicitud.folio}"
        holder.tvPlaca.text = "Observaciones: ${solicitud.observaciones}"
        // --- FIN DE LA CORRECCIÓN ---

        holder.tvFecha.visibility = View.GONE

        holder.itemView.setOnClickListener {
            onItemClick(solicitud)
        }
    }

    override fun getItemCount() = solicitudes.size

    fun updateData(newSolicitudes: List<GenerarArrastreAttributes>) { // <-- CAMBIA EL TIPO DE LISTA
        this.solicitudes = newSolicitudes
        notifyDataSetChanged()
    }
}
