package com.example.oscarapp.api

import com.example.oscarapp.model.PedidoLogin
import com.example.oscarapp.model.PedidoVoto
import com.example.oscarapp.model.RespostaLogin
import com.example.oscarapp.model.RespostaVoto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SistemaCentralApi {
    @POST("login")
    suspend fun entrar(@Body pedido: PedidoLogin): RespostaLogin

    @POST("votos")
    suspend fun confirmar(@Body pedido: PedidoVoto): RespostaVoto
}

val apiCentral: SistemaCentralApi = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(SistemaCentralApi::class.java)
