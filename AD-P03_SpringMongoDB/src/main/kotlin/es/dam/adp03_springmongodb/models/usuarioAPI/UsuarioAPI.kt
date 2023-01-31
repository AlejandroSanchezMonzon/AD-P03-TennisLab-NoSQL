package es.dam.adp03_springmongodb.models.usuarioAPI

import es.dam.adp03_springmongodb.models.usuarioAPI.Address
import es.dam.adp03_springmongodb.models.usuarioAPI.Company
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