package com.example.myapplication.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface VehiculoApiService {
    @POST("api/vehiculos")
    fun crearVehiculo(@Body body: RequestBody): Call<ResponseBody>
}