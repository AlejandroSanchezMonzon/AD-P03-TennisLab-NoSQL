package es.dam.adp03_springmongodb.dto

import es.dam.adp03_springmongodb.models.usuarioAPI.Address
import es.dam.adp03_springmongodb.models.usuarioAPI.Company
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioAPIDTO(
    val id: String,
    val address: Address?,
    val company: Company?,
    val email: String,
    val name: String,
    val phone: String?,
    val username: String,
    val website: String?
)

@Serializable
data class TareaAPIDTO(
    val id: String,
    val completed: Boolean,
    val title: String,
    val userId: Int
)