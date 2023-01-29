package es.dam.adp03_springmongodb.repositories.pedidos

import models.Pedido
import es.dam.adp03_springmongodb.repositories.CRUDRepository

interface IPedidosRepository: CRUDRepository<Pedido, String> {
}