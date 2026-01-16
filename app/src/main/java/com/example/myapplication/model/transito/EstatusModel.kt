package com.example.myapplication.model.transito

import com.example.myapplication.network.RetrofitSecureClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class EstatusModel {

    fun buscarPlacaEnServidor(
        placa: String,
        callback: (ResultadoConsulta) -> Unit
    ) {

        val call = RetrofitSecureClient
            .parquimetroApiService
            .consultarEstatusParquimetro(placa)

        call.enqueue(object : Callback<ParquimetroResponse> {

            override fun onResponse(
                call: Call<ParquimetroResponse>,
                response: Response<ParquimetroResponse>
            ) {

                // --- ERROR DE SERVIDOR ---
                if (!response.isSuccessful) {
                    callback(
                        ResultadoConsulta(
                            TipoResultado.ERROR_SERVIDOR,
                            "Error del servidor: ${response.code()}"
                        )
                    )
                    return
                }

                val registros = response.body()?.data

                // --- PLACA NO EXISTE ---
                if (registros.isNullOrEmpty()) {
                    callback(
                        ResultadoConsulta(
                            TipoResultado.NO_ENCONTRADO,
                            "NO ENCONTRADO\nLa placa no existe en el sistema."
                        )
                    )
                    return
                }

                val registro = registros.first()

                // ===============================
                // VALIDACIÓN DE FECHA
                // ===============================
                val hoy = LocalDate.now()
                val fechaRegistro = LocalDate.parse(registro.fecha)

                if (fechaRegistro.isBefore(hoy)) {
                    callback(
                        ResultadoConsulta(
                            TipoResultado.EXPIRADO,
                            "EXPIRADO\nEl parquímetro corresponde a una fecha anterior."
                        )
                    )
                    return
                }

                if (fechaRegistro.isAfter(hoy)) {
                    callback(
                        ResultadoConsulta(
                            TipoResultado.EXPIRADO,
                            "EXPIRADO\nEl parquímetro aún no inicia."
                        )
                    )
                    return
                }

                // ===============================
                // VALIDACIÓN DE HORARIO
                // ===============================
                val ahora = LocalTime.now()

                val horaInicio = LocalTime.parse(
                    registro.horaInicio.substring(0, 5)
                )

                val horaTermino = LocalTime.parse(
                    registro.horaTermino.substring(0, 5)
                )

                when {
                    ahora.isBefore(horaInicio) -> {
                        callback(
                            ResultadoConsulta(
                                TipoResultado.EXPIRADO,
                                "EXPIRADO\nEl parquímetro aún no inicia."
                            )
                        )
                    }

                    ahora.isAfter(horaTermino) -> {
                        callback(
                            ResultadoConsulta(
                                TipoResultado.EXPIRADO,
                                "EXPIRADO\nTiempo excedido."
                            )
                        )
                    }

                    else -> {
                        val minutosRestantes =
                            Duration.between(ahora, horaTermino).toMinutes()

                        callback(
                            ResultadoConsulta(
                                TipoResultado.VIGENTE,
                                "VIGENTE\nPlaca: ${registro.placa}\nTiempo restante: $minutosRestantes minutos"
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ParquimetroResponse>, t: Throwable) {
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
