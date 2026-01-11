package com.example.myapplication.model.usuario

data class User(
    val username: String,
    val email: String,
    val confirmed: Boolean,
    val blocked: Boolean,
    val role: Int
)