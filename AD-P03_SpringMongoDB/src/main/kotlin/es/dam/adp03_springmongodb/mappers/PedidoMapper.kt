/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.mappers

import es.dam.adp03_springmongodb.dto.PedidoDTO
import es.dam.adp03_springmongodb.models.Pedido

/**
 * Esta función de extensión de Pedido se ocupa de convertir al objeto de tipo transferencia de datos (DTO) para poder
 * pasar la información del modelo a ficheros de una forma más sencilla, evitando así los tipos complejos.
 *
 * @return PedidoDTO, el objeto convertido en DTO.
 */
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