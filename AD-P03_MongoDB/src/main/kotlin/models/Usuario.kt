package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Usuario(
    @BsonId
    val id: String = newId<Usuario>().toString(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val rol: TipoUsuario)

enum class TipoUsuario(){
    ADMIN_ENCARGADO,
    ADMIN_JEFE,
    ENCORDADOR,
    TENISTA
}
