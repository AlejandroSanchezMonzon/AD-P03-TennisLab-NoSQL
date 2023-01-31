package dto
import kotlinx.serialization.Serializable
import models.Tarea
import models.Turno
import models.Usuario
import models.usuarioAPI.Address
import models.usuarioAPI.Company
import models.usuarioAPI.UsuarioAPI
import kotlin.collections.ArrayList

@Serializable
data class UsuarioAPIDTO(
    val id: Int,
    val address: Address,
    val company: Company,
    val email: String,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)

@Serializable
data class TareaAPIDTO(
    val id: Int,
    val completed: Boolean,
    val title: String,
    val userId: Int
)