package es.dam.adp03_springmongodb.repositories.pedidos

import es.dam.adp03_springmongodb.models.Pedido
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface IPedidosRepository: MongoRepository<Pedido, ObjectId> {
}