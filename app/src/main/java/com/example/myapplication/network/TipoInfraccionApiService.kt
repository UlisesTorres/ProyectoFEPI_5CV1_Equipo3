package com.example.myapplication.network

import com.example.myapplication.model.transito.TipoInfraccionResponse
import retrofit2.Call
import retrofit2.http.GET

interface TipoInfraccionApiService {

    @GET("api/tipo-infraccions?populate[articulo_id][fields][0]=ordenamiento&populate[articulo_id][fields][1]=articulo_numero&populate[articulo_id][fields][2]=contenido&populate[articulo_id][fields][3]=ambito&populate[articulo_id][fields][4]=fecha_publicacion&populate[articulo_id][fields][5]=fecha_ultima_reforma")
    fun obtenerTiposInfraccion(): Call<TipoInfraccionResponse>
}
