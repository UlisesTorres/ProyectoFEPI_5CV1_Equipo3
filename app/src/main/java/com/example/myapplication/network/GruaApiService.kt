package com.example.myapplication.network

import com.example.myapplication.model.operador_grua.GenerarArrastreResponse
import retrofit2.Call
import retrofit2.http.GET

interface GruaApiService {

    /**
     * Obtiene la lista de solicitudes de la tabla 'generar-arrastres'.
     */
    @GET("api/generar-arrastres")
    fun getSolicitudesArrastre(): Call<GenerarArrastreResponse>
}
