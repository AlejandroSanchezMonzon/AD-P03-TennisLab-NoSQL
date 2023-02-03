/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.UUID

@Serializable
data class Tarea(
    @BsonId
    val id: String = newId<Tarea>().toString(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val precio: Float,
    val tipo: TipoTarea,
    val descripcion: String,
    val turno: Turno
)

enum class TipoTarea {
    PERSONALIZACION, ENCORDADO
}