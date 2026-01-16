package com.example.myapplication.model.corralones

import com.example.myapplication.network.RetrofitSecureClient
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
                            // Buscamos el estatus del pago dentro de la infracción
                            val estatusPago = infraccion?.pago?.estatus ?: "pendiente"
                            
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
