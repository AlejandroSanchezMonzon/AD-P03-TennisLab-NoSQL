package es.dam.adp03_springmongodb.dto

import kotlinx.serialization.Serializable

@Serializable
data class TurnoDTO(
    val id: String,
    val uuid: String,
    val comienzo: String,
    val final: String,
    val maquina: String,
    val encordador: String
)
