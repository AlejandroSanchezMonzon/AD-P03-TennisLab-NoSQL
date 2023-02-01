package mappers

import dto.UsuarioAPIDTO
import models.TipoUsuario
import models.Usuario
import utils.cifrarPassword
import utils.randomUserType
import java.util.*
import database.Usuario as UsuarioSQL
import models.Usuario as UsuarioModelo

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

fun UsuarioModelo.toUsuarioSQL(): UsuarioSQL {
    return UsuarioSQL(
        id = id.toLong(),
        uuid = uuid.toString(),
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password,
        rol = rol.toString()
    )
}

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

fun UsuarioModelo.toUsuarioAPIDTO(): UsuarioAPIDTO {
    return UsuarioAPIDTO(
        id = id.toInt(),
        address = null,
        company = null,
        name = nombre,
        username = apellido,
        email = email,
        phone = null,
        website = null
    )
}