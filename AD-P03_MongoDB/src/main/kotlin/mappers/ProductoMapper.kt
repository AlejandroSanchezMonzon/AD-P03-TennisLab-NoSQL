/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package mappers

import dto.ProductoDTO
import models.Producto

/**
 * Esta función de extensión de Producto se ocupa de convertir al objeto de tipos transferencia de datos (DTO) para poder
 * pasar la información del modelo a ficheros de una forma más sencilla, evitando así los tipos complejos.
 *
 * @return ProductoDTO, el objeto convertido en DTO.
 */
fun Producto.toProductoDTO(): ProductoDTO {
    return ProductoDTO(
        id = id,
        uuid = uuid.toString(),
        tipo = tipo.toString(),
        marca = marca,
        modelo = modelo,
        precio = precio,
        stock = stock
    )
}