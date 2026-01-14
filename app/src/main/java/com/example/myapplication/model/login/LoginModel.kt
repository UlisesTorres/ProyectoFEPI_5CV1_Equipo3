package com.example.myapplication.model.login

//LOGIN
data class LoginModel(
    val jwt: String,
    val user: Usuario
    )

data class Usuario(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val nombre_completo: String
)