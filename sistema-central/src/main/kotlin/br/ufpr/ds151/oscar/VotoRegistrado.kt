package br.ufpr.ds151.oscar

import org.springframework.jdbc.core.simple.JdbcClient

fun consultarVoto(banco: JdbcClient, usuarioId: Long): Map<String, String>? {
    return banco.sql("""
        SELECT filme_id, filme_nome, diretor_id, diretor_nome
        FROM votos WHERE usuario_id = :usuario
    """.trimIndent())
        .param("usuario", usuarioId)
        .query { linha, _ ->
            mapOf(
                "filmeId" to linha.getString("filme_id"),
                "filmeNome" to linha.getString("filme_nome"),
                "diretorId" to linha.getString("diretor_id"),
                "diretorNome" to linha.getString("diretor_nome")
            )
        }
        .optional().orElse(null)
}
