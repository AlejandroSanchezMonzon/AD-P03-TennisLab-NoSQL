package es.dam.adp03_springmongodb.mappers

import es.dam.adp03_springmongodb.dto.PedidoDTO
import es.dam.adp03_springmongodb.models.Pedido


fun Pedido.toPedidoDTO(): PedidoDTO {
    return PedidoDTO(
        id = id.toString(),
        uuid = uuid,
        tareas = tareas?.map { it.uuid }.toString(),
        productos = productos?.map { it.uuid }.toString(),
        estado = estado.toString(),
        usuario = usuario.uuid,
        fechaTope = fechaTope.toString(),
        fechaEntrada = fechaEntrada.toString(),
        fechaProgramada = fechaProgramada.toString(),
        fechaEntrega = fechaEntrega.toString(),
        precio = precio
    )
}