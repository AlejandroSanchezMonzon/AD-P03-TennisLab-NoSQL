package es.dam.adp03_springmongodb.mappers

import es.dam.adp03_springmongodb.dto.ProductoDTO
import es.dam.adp03_springmongodb.models.Producto


fun Producto.toProductoDTO(): ProductoDTO {
    return ProductoDTO(
        id = id.toString(),
        uuid = uuid,
        tipo = tipo.toString(),
        marca = marca,
        modelo = modelo,
        precio = precio,
        stock = stock
    )
}