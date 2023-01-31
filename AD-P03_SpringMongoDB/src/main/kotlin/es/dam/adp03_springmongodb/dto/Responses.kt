package es.dam.adp03_springmongodb.dto
import es.dam.adp03_springmongodb.models.Tarea
import es.dam.adp03_springmongodb.models.Turno
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.models.usuarioAPI.Address
import es.dam.adp03_springmongodb.models.usuarioAPI.Company
import kotlinx.serialization.Serializable

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
    val data: Usuario? = null)

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
    val turno: Turno
)

@Serializable
data class UpdateTareaDTO(
    val id: String,
    val uuid: String,
    val precio: Float,
    val descripcion: String,
    val tipo: String,
    val turno: Turno)