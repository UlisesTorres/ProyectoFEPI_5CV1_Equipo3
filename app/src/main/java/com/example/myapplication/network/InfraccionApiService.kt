package com.example.myapplication.network

import com.example.myapplication.model.corralones.CorralonSitioResponse
import com.example.myapplication.model.corralones.InventarioCorralonResponse
import com.example.myapplication.model.transito.InfraccionesResponse
import com.example.myapplication.model.transito.OrdenArrastreRequest
import com.example.myapplication.model.transito.TipoInfraccionResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface InfraccionApiService {

    @POST("api/infraccions")
    fun crearInfraccion(@Body body: RequestBody): Call<ResponseBody>

    @GET("api/infraccions")
    fun getInfracciones(@Query("populate") populate: String = "*"): Call<InfraccionesResponse>

    @GET("api/infraccions")
    fun buscarInfraccionPorFolio(
        @Query("filters[folio][\$eq]") folio: String
    ): Call<InfraccionesResponse>

    @PUT("api/infraccions/{id}")
    fun actualizarInfraccion(@Path("id") id: String, @Body body: RequestBody): Call<ResponseBody>

    // --- CORRALONES ---
    
    @GET("api/inventario-corralons")
    fun getInventarioReal(
        @Query("populate[infraccion_id][populate]") populate: String = "*"
    ): Call<InventarioCorralonResponse>

    @GET("api/inventario-corralons")
    fun buscarEnInventarioPorInfraccion(
        @Query("filters[infraccion_id][documentId][\$eq]") infraccionDocId: String
    ): Call<InventarioCorralonResponse>

    @POST("api/inventario-corralons")
    fun registrarEntradaCorralon(@Body body: RequestBody): Call<ResponseBody>

    @DELETE("api/inventario-corralons/{id}")
    fun liberarVehiculo(@Path("id") id: String): Call<ResponseBody>

    @GET("api/pagos") 
    fun verificarPago(@Query("filters[infraccion_id][id][\$eq]") infraccionId: Int): Call<ResponseBody>

    // --- OTROS ---
    @POST("api/generar-arrastres")
    fun crearOrdenArrastre(@Body body: OrdenArrastreRequest): Call<ResponseBody>

    @GET("api/tipo-infraccions")
    fun obtenerTiposInfraccion(@Query("populate") populate: String = "*"): Call<TipoInfraccionResponse>
}
