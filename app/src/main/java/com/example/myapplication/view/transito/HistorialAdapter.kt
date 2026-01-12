import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView // Importa TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R // Importa tus recursos
import com.example.myapplication.model.transito.InfraccionAttributes

class HistorialAdapter(
    private val infracciones: List<InfraccionAttributes>,
    private val onItemClick: (InfraccionAttributes) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.InfraccionViewHolder>() {

    // 1. Define el ViewHolder para que contenga las vistas de tu tarjeta
    inner class InfraccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFolio: TextView = itemView.findViewById(R.id.tvFolio)
        private val tvPlaca: TextView = itemView.findViewById(R.id.tvPlaca)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)

        fun bind(infraccion: InfraccionAttributes) {
            tvFolio.text = infraccion.folio
            tvPlaca.text = "Placa: ${infraccion.placa}" // Añadimos un texto descriptivo
            tvFecha.text = formatarFecha(infraccion.fecha) // Formatear la fecha se ve mejor

            itemView.setOnClickListener {
                onItemClick(infraccion)
            }
        }

        // Función de ejemplo para formatear la fecha, puedes mejorarla
        private fun formatarFecha(fechaJson: String): String {
            // Implementa una lógica más robusta si es necesario (ej. con SimpleDateFormat)
            return try {
                fechaJson.substring(0, 10) // Extrae solo YYYY-MM-DD
            } catch (e: Exception) {
                fechaJson // Si falla, muestra la fecha original
            }
        }
    }

    // 2. Infla (crea) el nuevo layout de la tarjeta
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfraccionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_infraccion_card, parent, false) // Usa tu nuevo layout
        return InfraccionViewHolder(view)
    }

    // 3. Vincula los datos con el ViewHolder
    override fun onBindViewHolder(holder: InfraccionViewHolder, position: Int) {
        val infraccion = infracciones[position]
        holder.bind(infraccion)
    }

    override fun getItemCount(): Int = infracciones.size
}
