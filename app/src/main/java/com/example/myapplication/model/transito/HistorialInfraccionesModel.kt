package com.example.myapplication.model.transito

import com.example.myapplication.network.RetrofitSecureClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialInfraccionesModel {

    fun consultarInfracciones(callback: (List<String>?, Boolean) -> Unit) {
        RetrofitSecureClient.infraccionApiService.getInfracciones().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val rawJson = response.body()?.string()
                    if (rawJson != null) {
                        val listaMapeada = parsearInfracciones(rawJson)
                        callback(listaMapeada, true)
                    } else {
                        callback(null, false)
                    }
                } else {
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(null, false)
            }
        })
    }

    private fun parsearInfracciones(json: String): List<String> {
        val lista = mutableListOf<String>()
        try {
            val root = JSONObject(json)
            val dataArray = root.getJSONArray("data")
            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val attributes = item.getJSONObject("attributes")
                
                val folio = attributes.optString("folio", "N/A")
                val placa = attributes.optString("placa_vehiculo", "N/A")
                val fecha = attributes.optString("fecha_infraccion", "N/A")
                
                lista.add("Folio: $folio | Placa: $placa | Fecha: $fecha")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lista
    }
}
