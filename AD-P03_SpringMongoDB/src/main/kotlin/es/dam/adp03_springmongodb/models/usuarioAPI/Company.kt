package es.dam.adp03_springmongodb.models.usuarioAPI

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)