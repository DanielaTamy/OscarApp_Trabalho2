package br.ufpr.ds151.oscar

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

class ErroApi(
    val estado: HttpStatus,
    val mensagem: String,
    val voto: Map<String, String>? = null
) : RuntimeException(mensagem)

@RestControllerAdvice
class ErrosApi {
    @ExceptionHandler(ErroApi::class)
    fun tratar(erro: ErroApi): ResponseEntity<Map<String, Any>> {
        val resposta = mutableMapOf<String, Any>("mensagem" to erro.mensagem)
        erro.voto?.let { resposta["voto"] = it }
        return ResponseEntity.status(erro.estado).body(resposta)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun jsonInvalido(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest()
            .body(mapOf("mensagem" to "Envie um objeto JSON válido."))
    }

    @ExceptionHandler(DataAccessException::class)
    fun falhaNoBanco(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("mensagem" to "Não foi possível acessar os dados."))
    }
}
