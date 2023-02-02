package es.dam.adp03_springmongodb.utils

import com.github.ajalt.mordant.terminal.Terminal
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.repositories.usuarios.UsuariosRestRepository
import kotlinx.coroutines.flow.toList

private val terminal = Terminal()
private val usuariosRestRepository = UsuariosRestRepository()

suspend fun logIn(): Usuario {
    var usuarioEncontrado: Usuario?

    println("¡Bienvenid@ a la gestión de TennisLab!")

    do {
        val email = terminal.prompt("Por favor, ingrese su correo.")?.trimIndent().orEmpty()
        val password = terminal.prompt("Por favor, ingrese su contraseña.")?.trimIndent().orEmpty()

        val usuariosAPI = usuariosRestRepository.findAll().toList()

        usuarioEncontrado = usuariosAPI.firstOrNull {
            it.email == email && it.password == cifrarPassword(password)
        }

        if (usuarioEncontrado != null) {
            println("¡Bienvenido ${usuarioEncontrado.nombre}!")
        } else {
            println("Email o contraseña incorrectos.")
        }

    } while (usuarioEncontrado == null)

    return usuarioEncontrado
}