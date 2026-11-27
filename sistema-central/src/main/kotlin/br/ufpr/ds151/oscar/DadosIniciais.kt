package br.ufpr.ds151.oscar

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@ConditionalOnProperty(name = ["oscar.dados-teste"], havingValue = "true")
open class DadosIniciais(
    private val banco: JdbcClient,
    private val codificador: BCryptPasswordEncoder
) : ApplicationRunner {
    @Transactional
    override fun run(argumentos: ApplicationArguments) {
        val senhaHash = codificador.encode("Oscar@29")
            ?: error("Não foi possível preparar a senha de demonstração.")
        for (numero in 1..5) {
            banco.sql("""
                INSERT INTO usuarios (login, senha_hash)
                VALUES (:login, :senha) ON CONFLICT (login) DO NOTHING
            """.trimIndent())
                .param("login", "jurado" + numero)
                .param("senha", senhaHash)
                .update()
        }
        banco.sql("""
            INSERT INTO filmes (id, nome, genero, foto)
            VALUES ('1', 'Piratas do Caribe', 'Aventura',
                    'http://200.236.3.97/imagens/piratas.jpeg')
            ON CONFLICT (id) DO NOTHING
        """.trimIndent()).update()
        banco.sql("""
            INSERT INTO diretores (id, nome)
            VALUES ('1', 'James Cameron') ON CONFLICT (id) DO NOTHING
        """.trimIndent()).update()
        banco.sql("""
            INSERT INTO votos (usuario_id, filme_id, filme_nome, diretor_id, diretor_nome)
            SELECT id, '1', 'Piratas do Caribe', '1', 'James Cameron'
            FROM usuarios WHERE login = 'jurado3'
            ON CONFLICT (usuario_id) DO NOTHING
        """.trimIndent()).update()
    }
}
