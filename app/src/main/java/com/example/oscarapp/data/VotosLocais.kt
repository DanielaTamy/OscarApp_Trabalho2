package com.example.oscarapp.data

import android.content.Context
import android.net.Uri
import com.example.oscarapp.model.Voto
import com.google.gson.Gson
import com.google.gson.JsonParseException
import java.io.File
import java.io.IOException

class VotosLocais(private val contexto: Context, login: String) {
    private val nomeArquivo = "votos_" + Uri.encode(login) + ".json"
    private val gson = Gson()

    fun ler(): Voto {
        if (!File(contexto.filesDir, nomeArquivo).exists()) return Voto()
        return try {
            contexto.openFileInput(nomeArquivo).bufferedReader().use { leitor ->
                gson.fromJson(leitor, Voto::class.java) ?: Voto()
            }
        } catch (erro: IOException) {
            Voto()
        } catch (erro: JsonParseException) {
            Voto()
        }
    }

    fun salvar(voto: Voto) {
        contexto.openFileOutput(nomeArquivo, Context.MODE_PRIVATE)
            .bufferedWriter().use { escritor ->
                escritor.write(gson.toJson(voto))
            }
    }

    fun apagar() {
        contexto.deleteFile(nomeArquivo)
    }
}
