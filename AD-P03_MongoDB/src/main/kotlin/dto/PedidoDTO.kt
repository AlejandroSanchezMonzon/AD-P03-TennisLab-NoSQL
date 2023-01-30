package dto

import kotlinx.serialization.Serializable

@Serializable
data class PedidoDTO(
    val id: String,
    val uuid: String,
    val tareas: String?,
    val productos: String?,
    val estado: String,
    val usuario: String,
    val fechaTope: String,
    val fechaEntrada: String,
    val fechaProgramada: String,
    val fechaEntrega: String,
    val precio: Float
)