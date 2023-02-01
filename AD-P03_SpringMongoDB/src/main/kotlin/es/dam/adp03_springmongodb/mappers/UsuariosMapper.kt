package es.dam.adp03_springmongodb.mappers

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
        id = ObjectId(id.toString().padStart(24, '0')),
        uuid = uuid,
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
        uuid = uuid,
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password,
        rol = rol.toString()
    )
}

fun UsuarioAPIDTO.toModelUsuario(): UsuarioModelo {
    return UsuarioModelo(
        id = ObjectId(id.toString().padStart(24, '0')),
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
        id = id.toString().toInt(),
        address = null,
        company = null,
        name = nombre,
        username = apellido,
        email = email,
        phone = null,
        website = null
    )
}