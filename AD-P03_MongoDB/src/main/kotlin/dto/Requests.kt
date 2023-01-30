package dto

import kotlinx.serialization.Serializable
import models.Turno

@Serializable
data class LoginDTO(
    val id: String,
    val uuid: String,
    val precio: Float,
    val descripcion: String,
    val tipo: String,
    val turno: Turno
)