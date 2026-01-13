package com.example.myapplication.model.operador_grua

// --- CORRECCIÓN #1: Añadimos la importación correcta ---
import com.example.myapplication.model.operador_grua.GenerarArrastreAttributes
import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SolicitudArrastreModel {

    // --- CORRECCIÓN #2: Actualizamos el tipo del callback ---
    // El callback ahora debe devolver una lista de 'GenerarArrastreAttributes'.
    fun obtenerPeticionesPendientes(callback: (solicitudes: List<GenerarArrastreAttributes>?, exito: Boolean) -> Unit) {
        val call = RetrofitSecureClient.gruaApiService.getSolicitudesArrastre()

        call.enqueue(object : Callback<GenerarArrastreResponse> {
            override fun onResponse(call: Call<GenerarArrastreResponse>, response: Response<GenerarArrastreResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    // Si la llamada fue exitosa, pasamos la lista de datos al callback.
                    // response.body()!!.data ahora es del tipo correcto (List<GenerarArrastreAttributes>),
                    // por lo que ya no habrá error.
                    callback(response.body()!!.data, true)
                } else {
                    // Si hubo un error en el servidor, pasamos null
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<GenerarArrastreResponse>, t: Throwable) {
                // Si hubo un error de red, pasamos null
                callback(null, false)
            }
        })
    }

    // --- CORRECCIÓN #3: Actualizamos el tipo del parámetro para el futuro ---
    // Aunque no la usamos ahora, es bueno dejarla correcta.
    fun marcarComoAceptada(solicitud: GenerarArrastreAttributes, callback: (Boolean) -> Unit) {
        // Lógica futura aquí
        callback(false)
    }
}
