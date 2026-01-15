package com.example.myapplication.network

import com.example.myapplication.model.operador_grua.GenerarArrastreResponse
import com.example.myapplication.model.operador_grua.OrdenArrastreRequest
import com.example.myapplication.model.operador_grua.OrdenArrastreResponse
import com.example.myapplication.model.operador_grua.UpdateArrastreRequest
import com.example.myapplication.model.operador_grua.UpdateArrastreResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GruaApiService {

    /**
     * Obtiene la lista de solicitudes de la tabla 'generar-arrastres'.
     * Usamos populate=* para asegurar que todas las relaciones (como infraccion_id) vengan en la respuesta.
     */
    @GET("api/generar-arrastres")
    fun getSolicitudesArrastre(
        @Query("populate") populate: String = "*"
    ): Call<GenerarArrastreResponse>


    /**
     * Crea una nueva orden de arrastre.
     */
    @POST("api/orden-arrastres")
    fun crearOrdenArrastre(@Body request: OrdenArrastreRequest): Call<OrdenArrastreResponse>

    @PUT("api/generar-arrastres/{documentId}")
    fun actualizarEstatusArrastre(
        @Path("documentId") documentId: String, // Cambiado de Int a String
        @Body request: UpdateArrastreRequest
    ): Call<UpdateArrastreResponse>
}
