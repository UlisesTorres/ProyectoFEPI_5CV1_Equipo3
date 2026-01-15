package com.example.myapplication.presenter.login

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
                    val username = json.getJSONObject("user").getString("username")
                    val email = json.getJSONObject("user").getString("email")
                    // Configuramos el token para futuras peticiones seguras
                    RetrofitSecureClient.setToken(jwt)

                    // 2. Ahora vamos a traer el Rol Real antes de navegar
                    obtenerRolRealYSaltar(jwt, userId, username, email)

                } else {
                    view.mostrarError("Credenciales inválidas")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.mostrarError("Error de red: ${t.message}")
            }
        })
    }

    private fun obtenerRolRealYSaltar(
        token: String,
        userId: Int,
        username: String,
        email: String
    ) {
        RetrofitAuthClient.authApiService.getMe("Bearer $token")
            .enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val json = JSONObject(response.body()?.string() ?: "")
                        val roleObj = json.optJSONObject("role")

                        if (roleObj != null) {
                            val roleName = roleObj.getString("name")
                            view.loginExitoso(token, userId, roleName, username, email)
                            decidirNavegacion(roleName)
                        } else {
                            view.mostrarError("Usuario sin rol asignado")
                        }
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
            else -> view.mostrarError("Rol no reconocido: $roleName")
        }
    }

    override fun clickRecuperarPassword() {
        // El presenter decide qué debe pasar
        view.mostrarAlertaRecuperar()
    }

}