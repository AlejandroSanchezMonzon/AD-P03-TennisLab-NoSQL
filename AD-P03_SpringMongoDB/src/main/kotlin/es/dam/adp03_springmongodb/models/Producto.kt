package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.util.*

@Serializable
data class Producto(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
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

