package com.example.myapplication.network

import com.example.myapplication.model.transito.InfraccionesResponse
import com.example.myapplication.model.transito.OrdenArrastreRequest
import com.example.myapplication.model.transito.TipoInfraccionResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface InfraccionApiService {

    @POST("api/infraccions")
    fun crearInfraccion(
        @Body body: RequestBody
    ): Call<ResponseBody>

    @GET("api/infraccions")
    fun getInfracciones(): Call<InfraccionesResponse>

    @POST("api/generar-arrastres") // Asegúrate de que el nombre coincide con tu colección en Strapi
    fun crearOrdenArrastre(
        @Body body: OrdenArrastreRequest
    ): Call<ResponseBody>

    @GET("api/tipo-infraccions")
    fun obtenerTiposInfraccion(
        @Query("populate") populate: String = "*"
    ): Call<TipoInfraccionResponse>

}
