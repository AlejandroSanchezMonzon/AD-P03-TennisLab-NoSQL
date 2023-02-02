/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Turno(
    @BsonId
    val id: String = newId<Turno>().toString(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    @Contextual
    val comienzo: LocalDateTime,
    @Contextual
    val final: LocalDateTime,
    val maquina: Maquina,
    val encordador: Usuario
)