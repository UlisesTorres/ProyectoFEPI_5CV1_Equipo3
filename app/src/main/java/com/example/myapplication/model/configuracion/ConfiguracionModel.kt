package com.example.myapplication.model.configuracion

import android.content.Context
import com.example.myapplication.network.RetrofitAuthClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfiguracionModel(private val context: Context) {

    fun obtenerDatosUsuario(): Triple<String, String, String> {
        val prefs = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)

        val nombre = prefs.getString("username", "Usuario") ?: "Usuario"
        val correo = prefs.getString("email", "correo@no-disponible") ?: ""
        val rol = prefs.getString("role", "SIN ROL") ?: "SIN ROL"

        return Triple(nombre, correo, rol)
    }

    fun limpiarDatosSesion(callback: (Boolean) -> Unit) {
        val prefs = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        callback(true)
    }

    fun verificarServidor(callback: (Boolean) -> Unit) {
        // Ping simple al backend usando /users/me
        RetrofitAuthClient.authApiService
            .getMe("") // El token ya va por interceptor si lo tienes
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    callback(response.isSuccessful)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(false)
                }
            })
    }
}

