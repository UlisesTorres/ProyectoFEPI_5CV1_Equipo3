package com.example.myapplication.network

import com.example.myapplication.model.operador_grua.GenerarArrastreResponse
import com.example.myapplication.model.operador_grua.OrdenArrastreRequest
import com.example.myapplication.model.operador_grua.OrdenArrastreResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GruaApiService {

    /**
     * Obtiene la lista de solicitudes de la tabla 'generar-arrastres'.
     * Usamos populate=* para asegurar que todas las relaciones (como infraccion_id) vengan en la respuesta.
     */
    @GET("api/generar-arrastres")
    fun getSolicitudesArrastre(@Query("populate") populate: String = "*"): Call<GenerarArrastreResponse>

    /**
     * Crea una nueva orden de arrastre.
     */
    @POST("api/orden-arrastres")
    fun crearOrdenArrastre(@Body request: OrdenArrastreRequest): Call<OrdenArrastreResponse>
}
