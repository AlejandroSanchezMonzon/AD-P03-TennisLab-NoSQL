package models.usuarioAPI

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)