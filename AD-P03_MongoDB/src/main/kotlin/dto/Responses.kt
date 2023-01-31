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
data class GetUsuarioByIdDTO(
    val data: Usuario?)

@Serializable
data class CreateUsuarioDTO(
    val id: String,
    val uuid: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val rol: String)

@Serializable
data class UpdateUsuarioDTO(
    val id: String,
    val uuid: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val rol: String)

@Serializable
data class GetAllTareasDTO(
    val data: ArrayList<Tarea>? = null)

@Serializable
data class GetTareaByIdDTO(
    val data: Tarea? = null)

@Serializable
data class CreateTareaDTO(
    val id: String,
    val uuid: String,
    val precio: Float,
    val descripcion: String,
    val tipo: String,
    val turno: Turno)

@Serializable
data class UpdateTareaDTO(
    val id: String,
    val uuid: String,
    val precio: Float,
    val descripcion: String,
    val tipo: String,
    val turno: Turno)