package com.example.oscarapp.model
import com.google.gson.annotations.SerializedName
data class Diretor(
    @SerializedName("id") val identificador: String,
    val nome: String
)