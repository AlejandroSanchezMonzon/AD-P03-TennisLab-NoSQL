package es.dam.adp03_springmongodb.models.usuarioAPI

import kotlinx.serialization.Serializable

@Serializable
data class Geo(
    val lat: String,
    val lng: String
)