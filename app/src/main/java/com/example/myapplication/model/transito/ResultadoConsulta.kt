package com.example.myapplication.model.transito

// Este enum nos ayudará a representar todos los posibles resultados de la consulta
enum class TipoResultado {
    VIGENTE,
    EXPIRADO,
    NO_ENCONTRADO,
    ERROR_SERVIDOR,
    ERROR_RED
}

// Esta clase envolverá el resultado para pasar más información al Presenter
data class ResultadoConsulta(
    val tipo: TipoResultado,
    val mensaje: String
)