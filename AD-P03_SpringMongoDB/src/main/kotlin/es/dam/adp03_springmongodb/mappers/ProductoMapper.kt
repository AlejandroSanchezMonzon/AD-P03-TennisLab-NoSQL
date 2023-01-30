package mappers

import dto.ProductoDTO
import models.Producto

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