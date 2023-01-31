package utils

import models.TipoTarea
import models.TipoUsuario

fun randomUserType(): TipoUsuario {
    return listOf(
        TipoUsuario.TENISTA,
        TipoUsuario.ADMIN_ENCARGADO,
        TipoUsuario.ADMIN_JEFE,
        TipoUsuario.ENCORDADOR)
        .random()
}

fun randomTareaType(): TipoTarea {
    return listOf(
        TipoTarea.ENCORDADO,
        TipoTarea.PERSONALIZACION
    ).random()
}