package com.example.myapplication.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PagoApiService {

    // Crear un pago
    @POST("api/pagos") // Asegúrate que esta ruta coincida con tu colección en Strapi
    fun crearPago(
        @Body body: RequestBody
    ): Call<ResponseBody>
}