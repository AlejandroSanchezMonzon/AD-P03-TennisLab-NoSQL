package models.usuarioAPI

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioAPI(
    val id: Int,
    val address: Address,
    val company: Company,
    val email: String,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)