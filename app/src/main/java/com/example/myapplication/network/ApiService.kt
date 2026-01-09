package com.example.myapplication.network

import com.example.myapplication.model.infracciones.InfraccionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import okhttp3.ResponseBody

interface ApiService {
    // Para obtener una infracción por folio

    /*
    @GET("api/infracciones")
    fun buscarInfraccion(
        @Query("filters[folio][\$eq]") folio: String
    ): Call<InfraccionResponse>
    */

    /*
    // Para crear la orden de arrastre que autoriza el policía
    @POST("api/ordenes-arrastre")
    fun crearOrdenArrastre(
        @Body orden: OrdenRequest
    ): Call<OrdenResponse>*/

    /*@GET("api/infracciones")
    fun testConexion(): Call<InfraccionResponse>*/
    @GET("api/users-permissions/permissions")
    fun testConexion(): Call<ResponseBody>

}