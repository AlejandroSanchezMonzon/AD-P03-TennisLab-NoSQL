/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.utils

import com.github.ajalt.mordant.terminal.Terminal
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.repositories.usuarios.IUsuariosRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

private val terminal = Terminal()

@Component
class Login {
    @Autowired
    private lateinit var usuariosRepository: IUsuariosRepository

    /**
     * Método encargado de buscar el usuario que esteé intentando iniciar sesión en el repositorio de Mongo para validar
     * que exista.
     *
     * @return Usuario, el usuario que se encuentra en la base de datos con los valores de email y contraseña que se especifiquen.
     */
    suspend fun logIn(): Usuario {
        var usuarioEncontrado: Usuario?

        println("¡Bienvenid@ a la gestión de TennisLab!")

        do {
            val email = terminal.prompt("Por favor, ingrese su correo.")?.trimIndent().orEmpty()
            val password = terminal.prompt("Por favor, ingrese su contraseña.")?.trimIndent().orEmpty()

            val usuariosAPI = usuariosRepository.findAll().toList()

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
}