package es.dam.adp03_springmongodb.repositories.productos

import models.Producto
import es.dam.adp03_springmongodb.repositories.CRUDRepository

interface IProductosRepository: CRUDRepository<Producto, String> {
}