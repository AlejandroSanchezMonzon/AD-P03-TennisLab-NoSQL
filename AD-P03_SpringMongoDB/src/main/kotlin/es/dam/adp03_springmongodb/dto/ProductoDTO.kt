package es.dam.adp03_springmongodb.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductoDTO(
    val id: String,
    val uuid: String,
    val tipo: String,
    val marca: String,
    val modelo: String,
    val precio: Float,
    val stock: Int
)