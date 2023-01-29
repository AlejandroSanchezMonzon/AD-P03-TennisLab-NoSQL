package repositories.productos

import models.Producto
import repositories.CRUDRepository

interface IProductosRepository: CRUDRepository<Producto, String> {
}