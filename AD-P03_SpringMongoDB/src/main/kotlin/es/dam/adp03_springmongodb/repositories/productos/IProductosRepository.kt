package es.dam.adp03_springmongodb.repositories.productos

import es.dam.adp03_springmongodb.models.Producto
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IProductosRepository: CoroutineCrudRepository<Producto, ObjectId> {
}