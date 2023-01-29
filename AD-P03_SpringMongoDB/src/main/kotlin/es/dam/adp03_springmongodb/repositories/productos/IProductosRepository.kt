package es.dam.adp03_springmongodb.repositories.productos

import es.dam.adp03_springmongodb.models.Producto
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface IProductosRepository: MongoRepository<Producto, ObjectId> {
}