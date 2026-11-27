package br.ufpr.ds151.oscar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication(proxyBeanMethods = false)
class SistemaCentral {
    @Bean
    fun codificador(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}

fun main(argumentos: Array<String>) {
    runApplication<SistemaCentral>(*argumentos)
}
