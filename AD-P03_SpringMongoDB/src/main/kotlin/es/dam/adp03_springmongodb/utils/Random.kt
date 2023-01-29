package es.dam.adp03_springmongodb.utils

import models.TipoUsuario

fun randomUseType(): TipoUsuario {
    return listOf(
        TipoUsuario.TENISTA,
        TipoUsuario.ADMIN_ENCARGADO,
        TipoUsuario.ADMIN_JEFE,
        TipoUsuario.ENCORDADOR)
        .random()
}