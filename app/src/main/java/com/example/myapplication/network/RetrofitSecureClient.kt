package com.example.myapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // <-- AÑADIR IMPORTACIÓN
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSecureClient {

    private const val BASE_URL =
        "https://apisistemainfracciones-production.up.railway.app/"

    private var token: String? = null

    fun setToken(jwt: String) {
        token = jwt
    }

    // INICIO DE CAMBIOS SUGERIDOS

    // 1. Crea el interceptor de logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Muestra toda la info de la petición/respuesta
    }

    private val okHttpClient = OkHttpClient.Builder()
        // 2. Tu interceptor para añadir el token va PRIMERO
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val requestBuilder = originalRequest.newBuilder()

            // Solo añade el header si el token no es nulo
            token?.let {
                android.util.Log.d("JWT_DEBUG", "Añadiendo token: Bearer $it")
                requestBuilder.header("Authorization", "Bearer $it")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        // 3. El interceptor de logging va DESPUÉS (casi al final)
        .addInterceptor(loggingInterceptor)
        .build()

    // FIN DE CAMBIOS SUGERIDOS

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // okHttpClient ahora incluye ambos interceptores
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
