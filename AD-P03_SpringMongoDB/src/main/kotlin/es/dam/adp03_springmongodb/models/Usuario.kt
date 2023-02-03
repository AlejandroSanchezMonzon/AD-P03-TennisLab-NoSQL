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
import java.util.*

@Document("usuarios")
@Serializable
data class Usuario(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: String = UUID.randomUUID().toString(),
    val nombre: String,
    val apellido: String,
    val email: String,
    var password: String,
    var rol: TipoUsuario
)

enum class TipoUsuario() {
    ADMIN_ENCARGADO,
    ADMIN_JEFE,
    ENCORDADOR,
    TENISTA
}
