/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.mappers

import es.dam.adp03_springmongodb.db.getTurnosInit
import es.dam.adp03_springmongodb.dto.TareaAPIDTO
import es.dam.adp03_springmongodb.models.Tarea
import es.dam.adp03_springmongodb.repositories.usuarios.UsuariosCacheRepository
import es.dam.adp03_springmongodb.services.sqldelight.SqlDeLightClient
import es.dam.adp03_springmongodb.utils.randomTareaType
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.*

val usuariosCacheRepository = UsuariosCacheRepository(SqlDeLightClient)

/**
 * Esta función de extensión de la Tarea de la API REST se ocupa de convertir los datos del objeto procedente de la API a modelo para
 * pasar la información al sistema y de esta forma poder trabajar con él tanto en la base de datos de Mongo como en la caché.
 *
 * @return Tarea, el objeto convertido a modelo.
 */
suspend fun TareaAPIDTO.toModelTarea(): Tarea {
    return Tarea(
        id = ObjectId(id.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        precio = (1..100).random().toFloat(),
        descripcion = title,
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
        id = id.toString(),
        completed = setCompleted(turno.final),
        title = tipo.toString(),
        userId = turno.encordador?.id.toString().toInt()
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