package mappers

import db.getTurnosInit
import dto.TareaAPIDTO
import models.Tarea
import utils.randomTareaType
import java.time.LocalDateTime
import java.util.*

suspend fun TareaAPIDTO.toModelTarea(): Tarea {
    return Tarea(
        id = id.toString(),
        uuid = UUID.randomUUID(),
        precio = (1..100).random().toFloat(),
        descripcion = title,
        tipo = randomTareaType(),
        turno = getTurnosInit().random()
    )
}

fun Tarea.toTareaAPIDTO(): TareaAPIDTO {
    return TareaAPIDTO(
        id = id.toInt(),
        completed = setCompleted(turno.final),
        title = tipo.toString(),
        userId = turno.encordador.id.toInt()
    )
}

fun setCompleted(final: LocalDateTime): Boolean {
    return !final.isAfter(LocalDateTime.now())
}