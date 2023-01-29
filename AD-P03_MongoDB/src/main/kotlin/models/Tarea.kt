package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.newId
import java.util.UUID

@Serializable
data class Tarea(
    val id: String = newId<Tarea>().toString(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val precio: Float,
    val descripcion: String,
    val tipo: TipoTarea,
    val turno: Turno
)

enum class TipoTarea {
    PERSONALIZACION, ENCORDADO
}