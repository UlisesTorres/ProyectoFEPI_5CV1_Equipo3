// Crea el archivo en: app/src/main/java/com/example/myapplication/network/ConsultaApiService.kt
package com.example.myapplication.network

import com.example.myapplication.model.licencia.LicenciaResponse
import com.example.myapplication.model.vehiculo.VehiculoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ConsultaApiService {

    @GET("api/vehiculos")
    fun consultarVehiculoPorPlaca(
        // Usamos filtros de Strapi: filters[campo][operador]=valor
        @Query("filters[placa_id][\$eq]") placa: String
    ): Call<VehiculoResponse>

    @GET("api/licencias")
    fun consultarLicencia(
        @Query("filters[num_licencia][\$eq]") numeroLicencia: String
    ): Call<LicenciaResponse>
}
