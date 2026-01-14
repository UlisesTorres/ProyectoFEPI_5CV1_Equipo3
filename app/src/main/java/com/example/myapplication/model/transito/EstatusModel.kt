package com.example.myapplication.model.transito

import com.example.myapplication.model.transito.ParquimetroResponse
import com.example.myapplication.model.transito.ResultadoConsulta
import com.example.myapplication.model.transito.TipoResultado
import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstatusModel {
    // El callback ahora devuelve un único objeto ResultadoConsulta
    fun buscarPlacaEnServidor(placa: String, callback: (ResultadoConsulta) -> Unit) {
        val call = RetrofitSecureClient.parquimetroApiService.consultarEstatusParquimetro(placa)

        call.enqueue(object : Callback<ParquimetroResponse> {
            override fun onResponse(call: Call<ParquimetroResponse>, response: Response<ParquimetroResponse>) {
                if (response.isSuccessful) {
                    val listaRegistros = response.body()?.data

                    if (!listaRegistros.isNullOrEmpty()) {
                        val primerRegistro = listaRegistros[0]
                        if (primerRegistro.vigente) {
                            // Caso VIGENTE
                            val mensaje = "Placa: ${primerRegistro.placaVehiculo}\nEstatus: VIGENTE\nZona: ${primerRegistro.zona}"
                            callback(ResultadoConsulta(TipoResultado.VIGENTE, mensaje))
                        } else {
                            // Caso CADUCADO
                            val mensaje = "Placa: ${primerRegistro.placaVehiculo}\nEstatus: CADUCADO\nZona: ${primerRegistro.zona}"
                            callback(ResultadoConsulta(TipoResultado.CADUCADO, mensaje))
                        }
                    } else {
                        // Caso NO_ENCONTRADO
                        callback(
                            ResultadoConsulta(
                                TipoResultado.NO_ENCONTRADO,
                                "Placa no encontrada en el sistema."
                            )
                        )
                    }
                } else {
                    // Caso ERROR_SERVIDOR
                    callback(
                        ResultadoConsulta(
                            TipoResultado.ERROR_SERVIDOR,
                            "Error del servidor: ${response.code()}"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ParquimetroResponse>, t: Throwable) {
                // Caso ERROR_RED
                callback(
                    ResultadoConsulta(
                        TipoResultado.ERROR_RED,
                        "Fallo de conexión: ${t.message}"
                    )
                )
            }
        })
    }
}