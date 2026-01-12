package com.example.myapplication.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSecureClient {

    private const val BASE_URL =
        "https://apisistemainfracciones-production.up.railway.app/"

    private var token: String? = null

    fun setToken(jwt: String) {
        token = jwt
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            // 🔍 LOG PARA VER SI EL TOKEN SE ESTÁ MANDANDO
            android.util.Log.d("JWT_DEBUG", "Bearer $token")

            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            chain.proceed(request)
        }
        .build()


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val infraccionApiService: InfraccionApiService by lazy {
        retrofit.create(InfraccionApiService::class.java)
    }

    val uploadApiService: UploadApiService by lazy {
        retrofit.create(UploadApiService::class.java)
    }
}
