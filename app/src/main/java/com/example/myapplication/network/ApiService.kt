package com.example.myapplication.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {
    @POST("api/auth/local")
    fun login(@Body credenciales: Map<String, String>): Call<ResponseBody>

    // 🔹 AGREGA ESTA LÍNEA:
    @GET("api/users/me?populate=role")
    fun getMe(@Header("Authorization") token: String): Call<ResponseBody>
}