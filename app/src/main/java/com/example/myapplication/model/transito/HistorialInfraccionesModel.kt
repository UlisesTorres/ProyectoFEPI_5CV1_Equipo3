package com.example.myapplication.model.transito

import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialInfraccionesModel {

    fun consultarInfracciones(callback: (List<InfraccionAttributes>?, Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.getInfracciones()
            .enqueue(object : Callback<InfraccionesResponse> {
                override fun onResponse(
                    call: Call<InfraccionesResponse>,
                    response: Response<InfraccionesResponse>
                ) {
                    if (response.isSuccessful) {
                        val infraccionesResponse = response.body()
                        // La lógica se simplifica: solo pasamos la lista de datos.
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