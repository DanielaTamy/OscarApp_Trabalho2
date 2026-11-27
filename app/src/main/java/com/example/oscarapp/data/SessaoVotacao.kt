package com.example.oscarapp.data

import com.example.oscarapp.model.Voto

object SessaoVotacao {
    var login: String? = null
        private set
    var token: Int? = null
        private set
    var identificadorSessao: String? = null
        private set
    var votoConfirmado: Voto? = null
        private set

    fun iniciar(login: String, token: Int, identificador: String?, voto: Voto?) {
        this.login = login
        this.token = token
        identificadorSessao = identificador
        votoConfirmado = voto
    }

    fun confirmar(voto: Voto) {
        votoConfirmado = voto
    }

    fun encerrar() {
        login = null
        token = null
        identificadorSessao = null
        votoConfirmado = null
    }
}
