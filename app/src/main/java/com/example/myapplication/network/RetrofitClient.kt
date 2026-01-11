package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://apisistemainfracciones-production.up.railway.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Instancia para consultas de texto (como buscar folios)
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Instancia para enviar la multa con fotos (Multipart)
    val infraccionApiService: InfraccionApiService by lazy {
        retrofit.create(InfraccionApiService::class.java)
    }

    val uploadApiService: UploadApiService by lazy {
        retrofit.create(UploadApiService::class.java)
    }
}