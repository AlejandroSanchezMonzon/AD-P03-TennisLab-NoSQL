package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Pedido(
    @BsonId
    val id: String = newId<Pedido>().toString(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val tareas: List<Tarea>?,
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
    val precio: Float)

enum class TipoEstado(){
    RECIBIDO,
    EN_PROCESO,
    TERMINADO
}
