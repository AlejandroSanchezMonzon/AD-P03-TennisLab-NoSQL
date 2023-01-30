package es.dam.adp03_springmongodb.utils

import es.dam.adp03_springmongodb.models.TipoUsuario

fun randomUserType(): TipoUsuario {
    return listOf(
        TipoUsuario.TENISTA,
        TipoUsuario.ADMIN_ENCARGADO,
        TipoUsuario.ADMIN_JEFE,
        TipoUsuario.ENCORDADOR)
        .random()
}