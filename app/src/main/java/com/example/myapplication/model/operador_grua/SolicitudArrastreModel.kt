package com.example.myapplication.model.operador_grua

import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SolicitudArrastreModel {

    fun obtenerPeticionesPendientes(callback: (solicitudes: List<GenerarArrastreAttributes>?, exito: Boolean) -> Unit) {
        val call = RetrofitSecureClient.gruaApiService.getSolicitudesArrastre()

        call.enqueue(object : Callback<GenerarArrastreResponse> {
            override fun onResponse(call: Call<GenerarArrastreResponse>, response: Response<GenerarArrastreResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val todasSolicitudes = response.body()!!.data
                    val solicitudesPendientes = todasSolicitudes.filter { it.estatus == 0 }

                    // Log para debug
                    solicitudesPendientes.forEach { solicitud ->
                        android.util.Log.d("MODEL", "DocumentId de ${solicitud.folio}: ${solicitud.documentId}")
                    }

                    callback(solicitudesPendientes, true)
                } else {
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<GenerarArrastreResponse>, t: Throwable) {
                callback(null, false)
            }
        })
    }

    private fun actualizarEstatusArrastre(documentId: String, callback: (Boolean) -> Unit) {
        android.util.Log.d("API_DEBUG", "Actualizando arrastre con documentId: $documentId")

        val updateRequest = UpdateArrastreRequest(
            data = UpdateArrastreData(estatus = 1)
        )

        val call = RetrofitSecureClient.gruaApiService.actualizarEstatusArrastre(documentId, updateRequest)

        call.enqueue(object : Callback<UpdateArrastreResponse> {
            override fun onResponse(call: Call<UpdateArrastreResponse>, response: Response<UpdateArrastreResponse>) {
                android.util.Log.d("API_DEBUG", "Response code: ${response.code()}")
                android.util.Log.d("API_DEBUG", "Response successful: ${response.isSuccessful}")

                if (!response.isSuccessful) {
                    android.util.Log.d("API_DEBUG", "Error body: ${response.errorBody()?.string()}")
                }

                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<UpdateArrastreResponse>, t: Throwable) {
                android.util.Log.e("API_DEBUG", "Error: ${t.message}")
                callback(false)
            }
        })
    }

    fun aceptarSolicitudDeArrastre(
        idArrastre: Int,
        documentId: String,  // Nuevo parámetro
        idInfraccion: Int,
        operadorGrua: String,
        gruaIdentificador: String,
        callback: (Boolean) -> Unit
    ) {
        android.util.Log.d("SOLICITUD", "Aceptando arrastre - ID: $idArrastre, DocumentId: $documentId")

        // PASO 1: Actualizar el estatus usando documentId
        actualizarEstatusArrastre(documentId) { estatusActualizado ->
            if (!estatusActualizado) {
                android.util.Log.w("SOLICITUD", "No se pudo actualizar estatus, continuando...")
                // Continuamos aunque falle para probar la creación de orden
            }

            // PASO 2: Crear la orden de arrastre
            val request = OrdenArrastreRequest(
                data = OrdenArrastreData(
                    fechaIngreso = getISODateTime(),
                    estatus = "en_corralon",
                    operadorGrua = operadorGrua,
                    gruaIdentificador = gruaIdentificador,
                    infraccionId = idInfraccion
                )
            )

            val call = RetrofitSecureClient.gruaApiService.crearOrdenArrastre(request)
            call.enqueue(object : Callback<OrdenArrastreResponse> {
                override fun onResponse(call: Call<OrdenArrastreResponse>, response: Response<OrdenArrastreResponse>) {
                    android.util.Log.d("SOLICITUD", "Orden creada: ${response.isSuccessful}")
                    callback(response.isSuccessful)
                }

                override fun onFailure(call: Call<OrdenArrastreResponse>, t: Throwable) {
                    android.util.Log.e("SOLICITUD", "Error creando orden: ${t.message}")
                    callback(false)
                }
            })
        }
    }

    private fun getISODateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
}