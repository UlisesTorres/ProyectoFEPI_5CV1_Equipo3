package com.example.myapplication.presenter.Login

import android.util.Log
import com.example.myapplication.network.RetrofitAuthClient
import com.example.myapplication.network.RetrofitSecureClient
import com.example.myapplication.view.login.LoginContract
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    override fun intentarLogin(usuario: String, pass: String) {
        val credenciales = mapOf(
            "identifier" to usuario.trim(),
            "password" to pass.trim()
        )

        RetrofitAuthClient.authApiService.login(credenciales).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val cuerpo = response.body()?.string()
                    val json = JSONObject(cuerpo ?: "")

                    // 1. Extraemos el Token y el ID (Aquí NO buscamos el role porque Strapi no lo envía)
                    val jwt = json.getString("jwt")
                    val userId = json.getJSONObject("user").getInt("id")

                    // Configuramos el token para futuras peticiones seguras
                    RetrofitSecureClient.setToken(jwt)

                    // 2. Ahora vamos a traer el Rol Real antes de navegar
                    obtenerRolRealYSaltar(jwt, userId)

                } else {
                    view.mostrarError("Credenciales inválidas")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.mostrarError("Error de red: ${t.message}")
            }
        })
    }

    private fun obtenerRolRealYSaltar(token: String, userId: Int) {
        RetrofitAuthClient.authApiService.getMe("Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val cuerpo = response.body()?.string()
                    val json = JSONObject(cuerpo ?: "")

                    // 🔹 USO DE optJSONObject PARA EVITAR CRASH
                    val roleObj = json.optJSONObject("role")

                    if (roleObj != null) {
                        val roleName = roleObj.getString("name")
                        Log.d("LOGIN_EXITO", "Rol encontrado: $roleName")
                        view.loginExitoso(token, userId, roleName)
                        decidirNavegacion(roleName)
                    } else {
                        // Si llega aquí, es porque Strapi respondió bien pero el campo 'role' sigue vacío
                        Log.e("LOGIN_ERROR", "El JSON no contiene el objeto 'role'. JSON completo: $json")
                        view.mostrarError("Error: El usuario no tiene un rol asignado o permitido.")
                    }
                } else {
                    Log.e("LOGIN_ERROR", "Error en getMe: ${response.code()}")
                    view.mostrarError("Error al obtener permisos")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.mostrarError("Error de conexión")
            }
        })
    }

    private fun decidirNavegacion(roleName: String) {
        // Usamos lowercase para evitar errores de dedos, pero comparamos con el nombre exacto de Strapi
        when (roleName) {
            "Oficial_Transito" -> view.navegarATransito()
            "Operador_Grua" -> view.navegarAGruas()
            "Supervisor" -> view.navegarASupervisor()
            "Gestor_Corralon" -> view.navegarAGestor()
            "Operador_Parquimetro" -> view.navegarAParquimetro()
            else -> view.mostrarError("Rol no reconocido: $roleName")
        }
    }
}