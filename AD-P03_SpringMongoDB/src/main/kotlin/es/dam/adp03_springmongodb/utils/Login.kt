package es.dam.adp03_springmongodb.utils

import com.github.ajalt.mordant.terminal.Terminal
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.repositories.usuarios.UsuariosCacheRepository
import es.dam.adp03_springmongodb.services.sqldelight.SqlDeLightClient


val usuariosCacheRepository = UsuariosCacheRepository(SqlDeLightClient)
val terminal = Terminal()

suspend fun logIn(): Usuario? {
    println("¡Bienvenid@ a la gestión de TennisLab!")
    val email = terminal.prompt("Por favor, ingrese su correo.")?.trimIndent().orEmpty()
    val password = terminal.prompt("Por favor, ingrese su contraseña.")?.trimIndent().orEmpty()

    var usuarioEncontrado : Usuario? = null
    usuariosCacheRepository.findAll().collect { usuarios ->
        usuarioEncontrado = usuarios.filter { usuario ->
            usuario.email == email && usuario.password == password
        }[0]
    }
    return usuarioEncontrado

}