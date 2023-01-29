package es.dam.adp03_springmongodb.dto
import kotlinx.serialization.Serializable
import models.Tarea
import models.Turno
import models.Usuario
import kotlin.collections.ArrayList


@Serializable
data class GetAllUsuariosDTO(
    val data: ArrayList<Usuario>? = null)

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
    val turno: Turno)

@Serializable
data class UpdateTareaDTO(
    val id: String,
    val uuid: String,
    val precio: Float,
    val descripcion: String,
    val tipo: String,
    val turno: Turno)