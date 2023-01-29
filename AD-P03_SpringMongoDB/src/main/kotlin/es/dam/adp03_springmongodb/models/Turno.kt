package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Turno(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    @Contextual
    val comienzo: LocalDateTime,
    @Contextual
    val final: LocalDateTime,
    val maquina: Maquina,
    val enrodador: Usuario
)