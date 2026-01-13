package com.example.myapplication.model.operador_grua

import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class SolicitudArrastreModel {

    fun obtenerPeticionesPendientes(callback: (solicitudes: List<GenerarArrastreAttributes>?, exito: Boolean) -> Unit) {
        val call = RetrofitSecureClient.gruaApiService.getSolicitudesArrastre()

        call.enqueue(object : Callback<GenerarArrastreResponse> {
            override fun onResponse(call: Call<GenerarArrastreResponse>, response: Response<GenerarArrastreResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.data, true)
                } else {
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<GenerarArrastreResponse>, t: Throwable) {
                callback(null, false)
            }
        })
    }

    fun aceptarSolicitudDeArrastre(idArrastre: Int, callback: (Boolean) -> Unit) {
        val request = OrdenArrastreRequest(
            data = OrdenArrastreData(
                fechaIngreso = getISODateTime(),
                // --- CORRECCIÓN: Usamos un valor de estatus válido ---
                estatus = "en_corralon",
                operadorGrua = "Operador 1", // TODO: Reemplazar con datos reales
                gruaIdentificador = "Grua-007", // TODO: Reemplazar con datos reales
                infraccionId = idArrastre
            )
        )

        val call = RetrofitSecureClient.gruaApiService.crearOrdenArrastre(request)
        call.enqueue(object : Callback<OrdenArrastreResponse> {
            override fun onResponse(call: Call<OrdenArrastreResponse>, response: Response<OrdenArrastreResponse>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<OrdenArrastreResponse>, t: Throwable) {
                callback(false)
            }
        })
    }

    private fun getISODateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
}
