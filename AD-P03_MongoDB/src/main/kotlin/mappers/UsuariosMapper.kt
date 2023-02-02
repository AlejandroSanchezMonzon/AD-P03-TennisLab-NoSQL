/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package mappers

import dto.UsuarioAPIDTO
import models.TipoUsuario
import models.Usuario
import utils.cifrarPassword
import utils.randomUserType
import java.util.*
import database.Usuario as UsuarioSQL
import models.Usuario as UsuarioModelo

/**
 * Esta función de extensión del Usuario de la base de datos se ocupa de convertir los datos del objeto del fichero .sq a modelo para
 * pasar la información al sistema y de esta forma poder trabajar con él tanto en la base de datos de Mongo como en la caché.
 *
 * @return UsuarioModelo, el objeto convertido a Usuario modelo.
 */
fun UsuarioSQL.toModel(): UsuarioModelo {
    return UsuarioModelo(
        id = id.toString(),
        uuid = UUID.fromString(uuid),
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password,
        rol = TipoUsuario.valueOf(rol)
    )
}

/**
 * Esta función de extensión del Usuario de la API REST se ocupa de convertir los datos del objeto procedente de la API a modelo para
 * pasar la información al sistema y de esta forma poder trabajar con él tanto en la base de datos de Mongo como en la caché.
 *
 * @return UsuarioModelo, el objeto convertido a Usuario modelo.
 */
fun UsuarioAPIDTO.toModelUsuario(): Usuario {
    return Usuario(
        id = id.toString(),
        uuid = UUID.randomUUID(),
        nombre = name,
        apellido = username,
        email = email,
        password = cifrarPassword(username),
        rol = randomUserType()
    )
}