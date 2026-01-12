package com.example.myapplication.network

import com.example.myapplication.model.transito.InfraccionesResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InfraccionApiService {

    @POST("api/infraccions")
    fun crearInfraccion(
        @Body body: RequestBody
    ): Call<ResponseBody>

    @GET("api/infraccions")
    fun getInfracciones(): Call<InfraccionesResponse>
}
