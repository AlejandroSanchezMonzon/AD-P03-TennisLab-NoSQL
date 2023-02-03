/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.repositories.pedidos

import es.dam.adp03_springmongodb.models.Pedido
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IPedidosRepository : CoroutineCrudRepository<Pedido, ObjectId> {
}