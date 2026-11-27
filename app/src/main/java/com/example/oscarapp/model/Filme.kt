package com.example.oscarapp.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class Filme(
    @SerializedName("id") val identificador: String,
    val nome: String,
    val genero: String,
    val foto: String
) : Serializable