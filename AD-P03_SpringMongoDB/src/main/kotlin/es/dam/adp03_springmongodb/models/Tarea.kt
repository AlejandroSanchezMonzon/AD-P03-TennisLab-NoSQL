package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.UUID

@Document("tareas")
@Serializable
data class Tarea(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val precio: Float,
    val descripcion: String,
    val tipo: TipoTarea,
    @DocumentReference()
    val turno: Turno
)

enum class TipoTarea {
    PERSONALIZACION, ENCORDADO
}