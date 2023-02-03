/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package mappers

import db.getTurnosInit
import dto.TareaAPIDTO
import models.Tarea
import utils.randomTareaType
import java.time.LocalDateTime
import java.util.*


/**
 * Esta función de extensión de la Tarea de la API REST se ocupa de convertir los datos del objeto procedente de la API a modelo para
 * pasar la información al sistema y de esta forma poder trabajar con él tanto en la base de datos de Mongo como en la caché.
 *
 * @return Tarea, el objeto convertido a modelo.
 */
suspend fun TareaAPIDTO.toModelTarea(): Tarea {
    return Tarea(
        id = id.toString(),
        uuid = UUID.randomUUID(),
        precio = (1..100).random().toFloat(),
        descripcion = title!!,
        tipo = randomTareaType(),
        turno = getTurnosInit().random()
    )
}

/**
 * Esta función de extensión sirve como mapeo del objeto Turno al Objeto TurnoAPIDTO, el cual usamos para trabajar con el en relación a la API.
 *
 * @return TareaAPIDTO, el objeto convertido del modelo.
 */
fun Tarea.toTareaAPIDTO(): TareaAPIDTO {
    return TareaAPIDTO(
        id = id.toInt(),
        completed = setCompleted(turno.final),
        title = descripcion,
        userId = turno.encordador.id.toInt()
    )
}

/**
 * Función que dependiendo de si la fecha dada es mayor o menor a la actual marca la tarea como completada o no.
 *
 * @param final Fecha dada.
 * @return  True o false, dependiendo de si ha sido completada o no.
 */
fun setCompleted(final: LocalDateTime): Boolean {
    return !final.isAfter(LocalDateTime.now())
}