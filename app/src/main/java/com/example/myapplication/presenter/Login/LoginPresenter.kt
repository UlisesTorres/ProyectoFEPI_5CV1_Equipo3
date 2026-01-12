package com.example.myapplication.presenter.Login

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

        RetrofitAuthClient.authApiService
            .login(credenciales)
            .enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {

                        val cuerpo = response.body()?.string()
                        val json = JSONObject(cuerpo ?: "")

                        val jwt = json.getString("jwt")
                        val userObj = json.getJSONObject("user")
                        val userId = userObj.getInt("id")
                        val username = userObj.getString("username")
                        RetrofitSecureClient.setToken(jwt)

                        // Guardar sesión
                        view.loginExitoso(jwt, userId)

                        // Navegación
                        decidirNavegacion(username)

                    } else {
                        val errorRaw = response.errorBody()?.string()
                        android.util.Log.e("LOGIN_DEBUG", "Código: ${response.code()}")
                        android.util.Log.e("LOGIN_DEBUG", "Error: $errorRaw")

                        view.mostrarError("Credenciales inválidas")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    view.mostrarError("Error de red")
                }
            })
    }

    private fun decidirNavegacion(username: String) {
        when {
            username.contains("oficial", true) -> view.navegarATransito()
            username.contains("gruero", true) -> view.navegarAGruas()
            username.contains("super", true) -> view.navegarASupervisor()
            username.contains("gestor", true) -> view.navegarAGestor()
            username.contains("parqui", true) -> view.navegarAParquimetro()
            else -> view.mostrarError("Usuario sin rol asignado")
        }
    }
}
