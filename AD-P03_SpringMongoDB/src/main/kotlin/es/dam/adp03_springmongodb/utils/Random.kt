/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.utils

import es.dam.adp03_springmongodb.models.TipoTarea
import es.dam.adp03_springmongodb.models.TipoUsuario

/**
 * Método encargado de devolver un tipo de usuario aleatoriamente.
 *
 * @return TipoUsuario, el tipo de usuario aleatorio.
 */
fun randomUserType(): TipoUsuario {

    return listOf(
        TipoUsuario.TENISTA,
        TipoUsuario.ADMIN_ENCARGADO,
        TipoUsuario.ADMIN_JEFE,
        TipoUsuario.ENCORDADOR
    )
        .random()
}

/**
 * Método encargado de devolver un tipo de tarea aleatoriamente.
 *
 * @return TipoTarea, el tipo de tarea aleatorio.
 */
fun randomTareaType(): TipoTarea {
    return listOf(
        TipoTarea.ENCORDADO,
        TipoTarea.PERSONALIZACION
    ).random()
}