package com.example.myapplication.model.corralones

import com.example.myapplication.model.transito.InfraccionData
import com.example.myapplication.network.RetrofitSecureClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

class LiberarModel {

    fun obtenerVehiculosParaLiberar(callback: (List<VehiculoInventario>?, Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.getInventarioReal()
            .enqueue(object : Callback<InventarioCorralonResponse> {
                override fun onResponse(call: Call<InventarioCorralonResponse>, response: Response<InventarioCorralonResponse>) {
                    if (response.isSuccessful) {
                        val lista = response.body()?.data?.map { data ->
                            val infraccion = data.infraccion
                            val estatusPago = extraerEstatusPago(infraccion)
                            
                            VehiculoInventario(
                                id = data.id.toString(),
                                documentId = data.documentId ?: "",
                                folio = infraccion?.folio ?: "S/F",
                                placa = infraccion?.placa_vehiculo ?: "S/P",
                                estatus = "En Depósito",
                                fechaIngreso = infraccion?.fecha_infraccion?.substringBefore("T") ?: "N/A",
                                ubicacion = data.nombre_corralon ?: "N/A",
                                observaciones = data.direccion_corralon ?: "",
                                pagoEstatus = estatusPago
                            )
                        }
                        callback(lista, true)
                    } else {
                        callback(null, false)
                    }
                }
                override fun onFailure(call: Call<InventarioCorralonResponse>, t: Throwable) {
                    callback(null, false)
                }
            })
    }

    private fun extraerEstatusPago(infraccion: InfraccionData?): String {
        val rawPago = infraccion?.pago ?: return "pendiente"
        return try {
            val gson = Gson()
            val jsonPago = gson.toJsonTree(rawPago).asJsonObject
            
            // Strapi v5 anida las relaciones en un objeto 'data'
            val data = jsonPago.get("data")
            if (data != null && !data.isJsonNull) {
                // Si es un objeto único
                if (data.isJsonObject) {
                    data.asJsonObject.get("attributes").asJsonObject.get("estatus").asString
                } else if (data.isJsonArray && data.asJsonArray.size() > 0) {
                    // Si viene como array (a veces Strapi lo hace)
                    data.asJsonArray.get(0).asJsonObject.get("attributes").asJsonObject.get("estatus").asString
                } else {
                    "pendiente"
                }
            } else {
                "pendiente"
            }
        } catch (e: Exception) {
            "pendiente"
        }
    }

    fun liberarVehiculo(documentId: String, callback: (Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.liberarVehiculo(documentId)
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
