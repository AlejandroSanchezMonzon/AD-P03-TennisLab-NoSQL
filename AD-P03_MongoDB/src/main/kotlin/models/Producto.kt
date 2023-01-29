package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Producto(
    @BsonId
    val id: String = newId<Producto>().toString(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val tipo: TipoProducto,
    val marca: String,
    val modelo: String,
    val precio: Float,
    val stock: Int)

enum class TipoProducto(){
    RAQUETA,
    CORDAJE,
    COMPLEMENTO
}

