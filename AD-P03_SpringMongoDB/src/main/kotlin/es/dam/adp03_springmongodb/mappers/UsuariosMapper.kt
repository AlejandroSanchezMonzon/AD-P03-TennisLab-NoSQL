/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.mappers

import es.dam.adp03_springmongodb.dto.UsuarioAPIDTO
import es.dam.adp03_springmongodb.models.TipoUsuario
import es.dam.adp03_springmongodb.utils.cifrarPassword
import es.dam.adp03_springmongodb.utils.randomUserType
import org.bson.types.ObjectId
import java.util.*
import database.Usuario as UsuarioSQL
import es.dam.adp03_springmongodb.models.Usuario as UsuarioModelo

/**
 * Esta función de extensión del Usuario de la base de datos se ocupa de convertir los datos del objeto del fichero .sq a modelo para
 * pasar la información al sistema y de esta forma poder trabajar con él tanto en la base de datos de Mongo como en la caché.
 *
 * @return UsuarioModelo, el objeto convertido a Usuario modelo.
 */
fun UsuarioSQL.toModel(): UsuarioModelo {
    return UsuarioModelo(
        id = ObjectId(id.padStart(24, '0')),
        uuid = uuid,
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
fun UsuarioModelo.toUsuarioSQL(): UsuarioSQL {
    return UsuarioSQL(
        id = id.toString(),
        uuid = uuid,
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password,
        rol = rol.toString()
    )
}

/**
 * Función de extensión que mapea el objeto Usuario al objeto UsuarioAPIDTO, el cual utilizamos para trabajar en la API.
 *
 * @return UsuarioAPIDTO, el objeto convertido del objeto Usuario.
 */
suspend fun UsuarioAPIDTO.toModelUsuario(): UsuarioModelo {
    return UsuarioModelo(
        id = ObjectId(id.padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = name,
        apellido = username,
        email = email,
        password = cifrarPassword(username),
        rol = randomUserType()
    )
}

fun UsuarioModelo.toUsuarioAPIDTO(): UsuarioAPIDTO {
    return UsuarioAPIDTO(
        id = id.toString(),
        address = null,
        company = null,
        name = nombre,
        username = apellido,
        email = email,
        phone = null,
        website = null
    )
}