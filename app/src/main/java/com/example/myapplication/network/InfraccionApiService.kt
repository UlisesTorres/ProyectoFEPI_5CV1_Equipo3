package com.example.myapplication.network

import com.example.myapplication.model.transito.InfraccionesResponse
import com.example.myapplication.model.transito.OrdenArrastreRequest
import com.example.myapplication.model.transito.TipoInfraccionResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface InfraccionApiService {

    @POST("api/infraccions")
    fun crearInfraccion(
        @Body body: RequestBody
    ): Call<ResponseBody>

    @GET("api/infraccions")
    fun getInfracciones(
        @Query("populate") populate: String = "*"
    ): Call<InfraccionesResponse>

    @PUT("api/infraccions/{id}")
    fun actualizarInfraccion(
        @Path("id") id: Int,
        @Body body: RequestBody
    ): Call<ResponseBody>

    @POST("api/generar-arrastres")
    fun crearOrdenArrastre(
        @Body body: OrdenArrastreRequest
    ): Call<ResponseBody>

    @GET("api/tipo-infraccions")
    fun obtenerTiposInfraccion(
        @Query("populate") populate: String = "*"
    ): Call<TipoInfraccionResponse>
}
