package mappers

import dto.PedidoDTO
import models.Pedido

fun Pedido.toPedidoDTO(): PedidoDTO {
    return PedidoDTO(
        id = id,
        uuid = uuid.toString(),
        tareas = tareas?.map { it.uuid }.toString(),
        productos = productos?.map { it.uuid }.toString(),
        estado = estado.toString(),
        usuario = usuario.uuid.toString(),
        fechaTope = fechaTope.toString(),
        fechaEntrada = fechaEntrada.toString(),
        fechaProgramada = fechaProgramada.toString(),
        fechaEntrega = fechaEntrega.toString(),
        precio = precio
    )
}