package com.example.myapplication.view.corralones

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.corralones.VehiculoInventario
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class LiberarAdapter(
    private var vehiculos: MutableList<VehiculoInventario>,
    private val onLiberarClick: (VehiculoInventario) -> Unit
) : RecyclerView.Adapter<LiberarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFolio: TextView = view.findViewById(R.id.tvFolioLiberar)
        val tvPlaca: TextView = view.findViewById(R.id.tvPlacaLiberar)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaLiberar)
        val chipPago: Chip = view.findViewById(R.id.chipPagoStatus)
        val btnLiberar: MaterialButton = view.findViewById(R.id.btnAccionLiberar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liberar_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val v = vehiculos[position]
        holder.tvFolio.text = "Folio: ${v.folio}"
        holder.tvPlaca.text = "Placa: ${v.placa}"
        holder.tvFecha.text = "Ingreso: ${v.fechaIngreso}"
        
        // Configurar el chip de pago según el estatus real
        when (v.pagoEstatus.lowercase()) {
            "pagado" -> {
                holder.chipPago.text = "PAGADO"
                holder.chipPago.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#4CAF50")) // Verde
                holder.btnLiberar.isEnabled = true
                holder.btnLiberar.alpha = 1.0f
            }
            "vencido" -> {
                holder.chipPago.text = "VENCIDO"
                holder.chipPago.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#F44336")) // Rojo
                holder.btnLiberar.isEnabled = false
                holder.btnLiberar.alpha = 0.5f
            }
            else -> { // pendiente
                holder.chipPago.text = "PENDIENTE"
                holder.chipPago.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FF9800")) // Naranja
                holder.btnLiberar.isEnabled = true
                holder.btnLiberar.alpha = 1.0f
            }
        }

        holder.btnLiberar.setOnClickListener { onLiberarClick(v) }
    }

    override fun getItemCount() = vehiculos.size

    fun updateData(newVehiculos: List<VehiculoInventario>) {
        vehiculos.clear()
        vehiculos.addAll(newVehiculos)
        notifyDataSetChanged()
    }

    fun removeVehiculo(documentId: String) {
        val index = vehiculos.indexOfFirst { it.documentId == documentId }
        if (index != -1) {
            vehiculos.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
