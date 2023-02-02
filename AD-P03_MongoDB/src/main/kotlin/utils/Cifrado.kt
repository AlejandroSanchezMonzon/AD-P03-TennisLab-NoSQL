/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package utils

import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

/**
 * Método encargado de cifrar bajo SHA256 la cadena de texto introducida por parámetros.
 *
 * @param password, cadena de texto a cifrar.
 *
 * @return String, cadena de texto cifrada.
 */
fun cifrarPassword(password: String): String {
    return Hashing.sha256()
        .hashString(password, StandardCharsets.UTF_8)
        .toString()
}