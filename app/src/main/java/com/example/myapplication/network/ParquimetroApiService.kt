package com.example.myapplication.network

import com.example.myapplication.model.parquimetros.ParquimetroResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de Retrofit dedicada exclusivamente a las operaciones
 * relacionadas con el sistema de parquímetros.
 */
interface ParquimetroApiService {


    @GET("api/consulta-parquimetros")
    fun consultarEstatusParquimetro( //
        @Query("filters[placa_vehiculo][\$eq]") placa: String
    ): Call<ParquimetroResponse>
}
