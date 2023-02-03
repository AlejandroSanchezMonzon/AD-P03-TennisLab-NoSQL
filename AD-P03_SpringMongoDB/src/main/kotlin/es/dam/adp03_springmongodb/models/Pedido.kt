/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate
import java.util.*

@Document("pedidos")
@Serializable
data class Pedido(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: String = UUID.randomUUID().toString(),
    @Field("tareas")
    @DocumentReference
    val tareas: List<Tarea>?,
    @Field("productos")
    @DocumentReference
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
    var fechaEntrega: LocalDate,
    val precio: Float
)

enum class TipoEstado {
    RECIBIDO,
    EN_PROCESO,
    TERMINADO
}
