package com.example.myapplication.model.corralones

import com.example.myapplication.model.transito.InfraccionData
import com.example.myapplication.network.RetrofitSecureClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventarioModel {
    
    fun obtenerAutosAlmacenados(callback: (List<VehiculoInventario>?, Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.getInventarioReal()
            .enqueue(object : Callback<InventarioCorralonResponse> {
                override fun onResponse(
                    call: Call<InventarioCorralonResponse>,
                    response: Response<InventarioCorralonResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        val listaMapeada = body?.data?.map { data ->
                            val infraccion = data.infraccion
                            val estatusPago = extraerEstatusPago(infraccion)
                            
                            VehiculoInventario(
                                id = data.id.toString(),
                                documentId = data.documentId ?: "",
                                folio = infraccion?.folio ?: "S/F",
                                placa = infraccion?.placa_vehiculo ?: "S/P",
                                estatus = "En Depósito",
                                fechaIngreso = infraccion?.fecha_infraccion?.substringBefore("T") ?: "N/A",
                                ubicacion = data.nombre_corralon ?: "Corralón",
                                observaciones = data.direccion_corralon ?: "Sin obs.",
                                pagoEstatus = estatusPago
                            )
                        }
                        callback(listaMapeada, true)
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
        // Buscamos en 'pago' o 'pagos' (plural)
        val rawPago = infraccion?.pago ?: infraccion?.pagos ?: return "pendiente"
        return try {
            val gson = Gson()
            val jsonPago = gson.toJsonTree(rawPago)
            
            val finalObj = if (jsonPago.isJsonObject) {
                val obj = jsonPago.asJsonObject
                // Caso Strapi v5 aplanado o con data/attributes
                if (obj.has("data") && !obj.get("data").isJsonNull) {
                    val data = obj.get("data")
                    if (data.isJsonObject) data.asJsonObject.get("attributes")?.asJsonObject ?: data.asJsonObject
                    else if (data.isJsonArray && data.asJsonArray.size() > 0) data.asJsonArray.get(0).asJsonObject.get("attributes")?.asJsonObject ?: data.asJsonArray.get(0).asJsonObject
                    else null
                } else {
                    obj.get("attributes")?.asJsonObject ?: obj
                }
            } else if (jsonPago.isJsonArray && jsonPago.asJsonArray.size() > 0) {
                val first = jsonPago.asJsonArray.get(0).asJsonObject
                first.get("attributes")?.asJsonObject ?: first
            } else {
                null
            }

            finalObj?.get("estatus")?.asString ?: "pendiente"
        } catch (e: Exception) {
            "pendiente"
        }
    }
}
