package mappers

import models.TipoUsuario
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