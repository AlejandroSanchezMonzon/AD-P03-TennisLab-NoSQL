package es.dam.adp03_springmongodb.dto

import es.dam.adp03_springmongodb.models.Turno
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    val id: Int,
    val uuid: String,
    val precio: Float,
    val descripcion: String,
    val tipo: String,
    val turno: Turno
)