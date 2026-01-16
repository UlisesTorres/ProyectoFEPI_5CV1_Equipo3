package com.example.myapplication.model.corralones

import com.example.myapplication.model.transito.InfraccionesResponse
import com.example.myapplication.network.RetrofitSecureClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarIngresoModel {

    fun consultarFolioEnApi(folio: String, callback: (id: Int?, documentId: String?, placa: String?, marcaModelo: String?, exito: Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.buscarInfraccionPorFolio(folio)
            .enqueue(object : Callback<InfraccionesResponse> {
                override fun onResponse(call: Call<InfraccionesResponse>, response: Response<InfraccionesResponse>) {
                    val infraccion = response.body()?.data?.firstOrNull()
                    if (response.isSuccessful && infraccion != null) {
                        val marca = infraccion.marca ?: ""
                        val modelo = infraccion.modelo ?: ""
                        val marcaModelo = if (marca.isNotEmpty() || modelo.isNotEmpty()) "$marca $modelo" else "Desconocido"
                        callback(infraccion.id, infraccion.documentId, infraccion.placa_vehiculo, marcaModelo, true)
                    } else {
                        callback(null, null, null, null, false)
                    }
                }
                override fun onFailure(call: Call<InfraccionesResponse>, t: Throwable) {
                    callback(null, null, null, null, false)
                }
            })
    }

    fun verificarSiYaEstaEnCorralon(documentId: String, callback: (Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.buscarEnInventarioPorInfraccion(documentId)
            .enqueue(object : Callback<InventarioCorralonResponse> {
                override fun onResponse(call: Call<InventarioCorralonResponse>, response: Response<InventarioCorralonResponse>) {
                    val yaExiste = response.body()?.data?.isNotEmpty() ?: false
                    callback(yaExiste)
                }
                override fun onFailure(call: Call<InventarioCorralonResponse>, t: Throwable) {
                    callback(false)
                }
            })
    }

    fun registrarIngreso(
        documentId: String,
        nombreCorralon: String,
        direccion: String,
        callback: (Boolean) -> Unit
    ) {
        val json = JSONObject().apply {
            val data = JSONObject().apply {
                put("nombre_corralon", nombreCorralon)
                put("direccion_corralon", direccion)
                put("infraccion_id", documentId) // Usamos documentId para la relación en Strapi v5
                // Eliminamos marca_modelo y observaciones por ser rechazadas por el backend
            }
            put("data", data)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        RetrofitSecureClient.infraccionApiService.registrarEntradaCorralon(requestBody)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    callback(response.isSuccessful)
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(false)
                }
            })
    }
}
