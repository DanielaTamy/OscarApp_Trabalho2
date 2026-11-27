package com.example.oscarapp.model
data class RespostaLogin(
    val login: String?,
    val sessao: String?,
    val token: Int?,
    val voto: Voto?
)