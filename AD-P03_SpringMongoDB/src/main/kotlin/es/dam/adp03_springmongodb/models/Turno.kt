package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime
import java.util.*

@Document("turnos")
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
    @DocumentReference()
    val maquina: Maquina,
    @DocumentReference()
    val encordador: Usuario
)