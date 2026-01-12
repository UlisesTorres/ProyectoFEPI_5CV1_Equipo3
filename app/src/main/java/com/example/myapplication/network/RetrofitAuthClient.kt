package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAuthClient {

    private const val BASE_URL =
        "https://apisistemainfracciones-production.up.railway.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
