package com.example.myapplication.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://apisistemainfracciones-production.up.railway.app/"

    // El token que te pasaron (asegúrate de que no tenga espacios al inicio o final)
    private const val STRAPI_TOKEN = "52389a0b9fbb4e80def7637a3fbef335da74af4e8de45e7e8c20ee3f11e074cf51c3124a29738a07f99b4ef397c169976b33211765815eae01f19e05e862a7078a75ced9314f8356f014491bacec32d4057bb48c3b92f4f015e38768c4ce6f358d28919a41cb7dfa80b28692471bf5ea67bff9ad63701a488cf5d08bbe747745"

    // Configuramos el cliente para que incluya el Token automáticamente
    private val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer $STRAPI_TOKEN") // Aquí se inyecta el token
            .method(original.method, original.body)
        val request = requestBuilder.build()
        chain.proceed(request)
    }.build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <--- IMPORTANTE: Usar el cliente con seguridad
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
    val infraccionApiService: InfraccionApiService by lazy { retrofit.create(InfraccionApiService::class.java) }
    val uploadApiService: UploadApiService by lazy { retrofit.create(UploadApiService::class.java) }
}