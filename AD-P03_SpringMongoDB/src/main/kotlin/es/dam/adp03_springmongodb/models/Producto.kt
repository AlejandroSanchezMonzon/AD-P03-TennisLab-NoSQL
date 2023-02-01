package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("productos")
@Serializable
data class Producto(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: String = UUID.randomUUID().toString(),
    val tipo: TipoProducto,
    val marca: String,
    val modelo: String,
    val precio: Float,
    val stock: Int
)

enum class TipoProducto() {
    RAQUETA,
    CORDAJE,
    COMPLEMENTO
}

