package com.example.myapplication.model.Articulo

data class ArticuloModel(
    val ordenamiento: String,
    val articulo_numero: Int,
    val contenido: String,
    val ambito: String,
    val fecha_publicacion: String,
    val fecha_ultima_reforma: String?
)