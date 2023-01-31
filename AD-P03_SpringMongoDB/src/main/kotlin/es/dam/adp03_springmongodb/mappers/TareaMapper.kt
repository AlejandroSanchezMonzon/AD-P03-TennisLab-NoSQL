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

suspend fun TareaAPIDTO.toModelTarea(): Tarea {
    return Tarea(
        id = ObjectId(id.toString()),
        uuid = UUID.randomUUID(),
        precio = (1..100).random().toFloat(),
        descripcion = title,
        tipo = randomTareaType(),
        turno = getTurnosInit(usuariosCacheRepository).random()
    )
}

fun Tarea.toTareaAPIDTO(): TareaAPIDTO {
    return TareaAPIDTO(
        id = id.toString().toInt(),
        completed = setCompleted(turno.final),
        title = tipo.toString(),
        userId = turno.encordador.id.toString().toInt()
    )
}

fun setCompleted(final: LocalDateTime): Boolean {
    return !final.isAfter(LocalDateTime.now())
}