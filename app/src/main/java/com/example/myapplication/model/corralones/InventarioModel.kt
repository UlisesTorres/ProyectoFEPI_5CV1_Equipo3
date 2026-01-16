package com.example.myapplication.model.corralones

import android.util.Log
import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventarioModel {
    
    fun obtenerAutosAlmacenados(callback: (List<VehiculoInventario>?, Boolean) -> Unit) {
        // Corregido: populate profundo para obtener el pago de la infracción
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
                            // El estatus ahora viene anidado en la infracción
                            val estatusPago = infraccion?.pago?.estatus ?: "pendiente"
                            
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
}
