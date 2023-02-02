/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package utils

import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import models.Usuario
import repositories.usuarios.UsuariosRepository

val usuariosRepository = UsuariosRepository()
val terminal = Terminal()


/**
 * Método encargado de buscar el usuario que esteé intentando iniciar sesión en el repositorio de Mongo para validar
 * que exista.
 *
 * @return Usuario, el usuario que se encuentra en la base de datos con los valores de email y contraseña que se especifiquen.
 */
suspend fun logIn(): Usuario {
    println("¡Bienvenid@ a la gestión de TennisLab!")
    var usuarioEncontrado: Usuario?
    do {
        val email = terminal.prompt("Por favor, ingrese su correo")?.trimIndent().orEmpty()
        val password = terminal.prompt("Por favor, ingrese su contraseña")?.trimIndent().orEmpty()

        usuarioEncontrado = usuariosRepository.findAll().filter { usuario ->
            usuario.email == email && usuario.password == cifrarPassword(password)
        }.firstOrNull()

        if (usuarioEncontrado != null) {
            println("¡Bienvenido ${usuarioEncontrado.nombre}!")

        } else {
            println("Email o contraseña incorrectos.")

        }
    }while(usuarioEncontrado==null)
    return usuarioEncontrado


}