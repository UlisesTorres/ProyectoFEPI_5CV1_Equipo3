package com.example.myapplication.model.transito

import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialInfraccionesModel {

    // Cambiamos el callback para que devuelva la lista de InfraccionData (que incluye ID y Atributos)
    fun consultarInfracciones(callback: (List<InfraccionData>?, Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.getInfracciones()
            .enqueue(object : Callback<InfraccionesResponse> {
                override fun onResponse(
                    call: Call<InfraccionesResponse>,
                    response: Response<InfraccionesResponse>
                ) {
                    if (response.isSuccessful) {
                        val infraccionesResponse = response.body()
                        callback(infraccionesResponse?.data, infraccionesResponse?.data != null)
                    } else {
                        callback(null, false)
                    }
                }

                override fun onFailure(call: Call<InfraccionesResponse>, t: Throwable) {
                    callback(null, false)
                }
            })
    }
}
