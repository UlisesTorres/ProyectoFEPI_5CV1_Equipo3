package com.example.myapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSecureClient {

    private const val BASE_URL =
        "https://apisistemainfracciones-production.up.railway.app/"

    private var token: String? = null

    fun setToken(jwt: String) {
        token = jwt
    }

    // ... (toda tu configuración de loggingInterceptor y okHttpClient se queda igual) ...
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            token?.let {
                android.util.Log.d("JWT_DEBUG", "Añadiendo token: Bearer $it")
                requestBuilder.header("Authorization", "Bearer $it")
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- Servicios existentes ---

    val infraccionApiService: InfraccionApiService by lazy {
        retrofit.create(InfraccionApiService::class.java)
    }

    val uploadApiService: UploadApiService by lazy {
        retrofit.create(UploadApiService::class.java)
    }

    val parquimetroApiService: ParquimetroApiService by lazy {
        retrofit.create(ParquimetroApiService::class.java)
    }

    val gruaApiService: GruaApiService by lazy {
        retrofit.create(GruaApiService::class.java)
    }

    val consultaApiService: ConsultaApiService by lazy {
        retrofit.create(ConsultaApiService::class.java)
    }

    val tipoInfraccionApiService: TipoInfraccionApiService by lazy {
        retrofit.create(TipoInfraccionApiService::class.java)
    }

    val vehiculoApiService: VehiculoApiService by lazy {
        retrofit.create(VehiculoApiService::class.java)
    }

    val licenciaApiService: LicenciaApiService by lazy {
        retrofit.create(LicenciaApiService::class.java)
    }

    val pagoApiService: PagoApiService by lazy {
        retrofit.create(PagoApiService::class.java)
    }

}
