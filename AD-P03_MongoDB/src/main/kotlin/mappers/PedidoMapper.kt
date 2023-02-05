/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package mappers

import dto.PedidoDTO
import models.Pedido

/**
 * Esta función de extensión de Pedido se ocupa de convertir al objeto de tipo transferencia de datos (DTO) para poder
 * pasar la información del modelo a ficheros de una forma más sencilla, evitando así los tipos complejos.
 *
 * @return PedidoDTO, el objeto convertido en DTO.
 */
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