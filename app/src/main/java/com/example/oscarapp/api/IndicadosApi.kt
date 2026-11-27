package com.example.oscarapp.api

import com.example.oscarapp.model.Diretor
import com.example.oscarapp.model.Filme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IndicadosApi {
    @GET("filme.json")
    suspend fun buscarFilmes(): List<Filme>

    @GET("diretor.json")
    suspend fun buscarDiretores(): List<Diretor>
}

val apiIndicados: IndicadosApi = Retrofit.Builder()
    .baseUrl("http://200.236.3.97/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(IndicadosApi::class.java)
