package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDate
import java.util.*

@Document("pedidos")
@Serializable
data class Pedido(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: String = UUID.randomUUID().toString(),
    @ReadOnlyProperty
    @DocumentReference()
    val tareas: List<Tarea>?,
    @ReadOnlyProperty
    @DocumentReference()
    val productos: List<Producto>?,
    val estado: TipoEstado,
    val usuario: Usuario,
    @Contextual
    val fechaTope: LocalDate,
    @Contextual
    val fechaEntrada: LocalDate,
    @Contextual
    val fechaProgramada: LocalDate,
    @Contextual
    val fechaEntrega: LocalDate,
    val precio: Float
)

enum class TipoEstado() {
    RECIBIDO,
    EN_PROCESO,
    TERMINADO
}
