package br.ufpr.ds151.oscar

import java.security.SecureRandom
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
open class Autenticacao(
    private val banco: JdbcClient,
    private val codificador: BCryptPasswordEncoder
) {
    private data class Usuario(val id: Long, val senhaHash: String)
    private val sorteio = SecureRandom()

    @PostMapping("/login")
    @Transactional
    open fun entrar(@RequestBody pedido: Map<String, Any?>): Map<String, Any?> {
        val login = (pedido["login"] as? String).orEmpty().trim()
        val senha = (pedido["senha"] as? String).orEmpty()
        if (login.isBlank() || login.length > 80 || senha.isBlank() ||
            senha.toByteArray(Charsets.UTF_8).size > 72
        ) {
            throw ErroApi(HttpStatus.BAD_REQUEST, "Informe login e senha válidos.")
        }
        val usuario = banco.sql(
            "SELECT id, senha_hash FROM usuarios WHERE login = :login FOR UPDATE"
        ).param("login", login)
            .query { linha, _ -> Usuario(linha.getLong("id"), linha.getString("senha_hash")) }
            .optional().orElse(null)
        if (usuario == null || !codificador.matches(senha, usuario.senhaHash)) {
            throw ErroApi(HttpStatus.UNAUTHORIZED, "Login ou senha incorretos.")
        }
        val tokenAnterior = banco.sql("SELECT token FROM sessoes WHERE usuario_id = :usuario")
            .param("usuario", usuario.id)
            .query(Int::class.javaObjectType)
            .optional().orElse(null)
        val token = if (tokenAnterior == null) sorteio.nextInt(101)
            else (tokenAnterior + 1 + sorteio.nextInt(100)) % 101
        val sessao = UUID.randomUUID()
        banco.sql("""
            INSERT INTO sessoes (usuario_id, identificador, token, expira_em)
            VALUES (:usuario, :sessao, :token, CURRENT_TIMESTAMP + INTERVAL '2 hours')
            ON CONFLICT (usuario_id) DO UPDATE SET
                identificador = EXCLUDED.identificador,
                token = EXCLUDED.token,
                expira_em = EXCLUDED.expira_em
        """.trimIndent())
            .param("usuario", usuario.id)
            .param("sessao", sessao)
            .param("token", token)
            .update()
        return mapOf(
            "login" to login,
            "sessao" to sessao.toString(),
            "token" to token,
            "voto" to consultarVoto(banco, usuario.id)
        )
    }
}
