package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document("tareas")
@Serializable
data class Tarea(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: String = UUID.randomUUID().toString(),
    val precio: Float,
    val descripcion: String,
    val tipo: TipoTarea,
    @DocumentReference()
    @Field("turno")
    val turno: Turno
)

enum class TipoTarea {
    PERSONALIZACION, ENCORDADO
}