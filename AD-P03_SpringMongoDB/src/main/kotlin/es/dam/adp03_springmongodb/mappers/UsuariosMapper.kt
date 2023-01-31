package mappers

import es.dam.adp03_springmongodb.dto.UsuarioAPIDTO
import es.dam.adp03_springmongodb.models.TipoUsuario
import es.dam.adp03_springmongodb.utils.cifrarPassword
import es.dam.adp03_springmongodb.utils.randomUserType
import org.bson.types.ObjectId
import java.util.*
import database.Usuario as UsuarioSQL
import es.dam.adp03_springmongodb.models.Usuario as UsuarioModelo

fun UsuarioSQL.toModel(): UsuarioModelo {
    return UsuarioModelo(
        id = ObjectId(id.toString()),
        uuid = UUID.fromString(uuid),
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password,
        rol = TipoUsuario.valueOf(rol)
    )
}

fun UsuarioModelo.toUsuarioSQL(): UsuarioSQL {
    return UsuarioSQL(
        id = id.toString().toLong(),
        uuid = uuid.toString(),
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password,
        rol = rol.toString()
    )
}

fun UsuarioAPIDTO.toModelUsuario(): UsuarioModelo {
    return UsuarioModelo(
        id = ObjectId(id.toString()),
        uuid = UUID.randomUUID(),
        nombre = name,
        apellido = username,
        email = email,
        password = cifrarPassword(username),
        rol = randomUserType()
    )
}