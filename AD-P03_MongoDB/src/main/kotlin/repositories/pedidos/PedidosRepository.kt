/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.pedidos

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import models.Maquina
import models.Pedido
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

@Single
@Named("PedidosRepository")
class PedidosRepository: IPedidosRepository {

    /**
     * Método encargado de utilizar el objeto MongoDbManager para acceder a la base de datos de Mongo y a través
     * de la búsqueda de la coleccion Pedido, ejecutar un método que devuelve un iterable de todos los objetos que hay en esa colección.
     *
     * @return Flow<Pedido>, el flujo de objetos encontrados en la colección.
     */
    override suspend fun findAll(): Flow<Pedido> {
        logger.debug { "findAll()" }
        return MongoDbManager.database.getCollection<Pedido>().find().asFlow()
    }

    /**
     * Método encargado de utilizar el objeto MongoDbManager para acceder a la base de datos de Mongo y a través
     * de la búsqueda de la coleccion Pedido, ejecutar un método que devuelve el primer objeto con un identificador dado.
     *
     * @param id identificador de tipo String del objeto a consultar.
     *
     * @return Pedido?, el objeto que tiene el identificador introducido por parámetros, si no se encuentra, devolverá nulo.
     */
    override suspend fun findById(id: String): Pedido? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Pedido>()
            .findOneById(id)
    }

    /**
     * Método encargado de utilizar el objeto MongoDbManager para acceder a la base de datos de Mongo y a través
     * de la búsqueda de la coleccion Pedido, ejecutar un método que no devuelve ni lanza ninguna excepción si
     * la operación ha sido exitosa.
     *
     * @param entity Objeto a insetar en la base de datos.
     *
     * @return Pedido, el objeto que ha sido insertado.
     */
    override suspend fun save(entity: Pedido): Pedido {
        logger.debug { "save($entity) - guardando" }
        return MongoDbManager.database.getCollection<Pedido>()
            .save(entity).let { entity }    }

    /**
     * Método encargado de utilizar el objeto MongoDbManager para acceder a la base de datos de Mongo y a través
     * de la búsqueda de la coleccion Pedido, ejecutar un método que no devuelve ni lanza ninguna excepción si
     * la operación ha sido exitosa. La operación realizará una actualización del objeto cuyo identificador sea
     * el mismo que el del objeto introducido por parámetros y en caso de que no lo encuentré añadirá un nuevo objeto
     * con sus valores.
     *
     * @param entity Objeto a actualizar en la base de datos.
     *
     * @return Pedido, el objeto que ha sido actualizado o insertado.
     */
    override suspend fun update(entity: Pedido): Pedido {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Pedido>()
            .save(entity).let { entity }    }

    /**
     * Método encargado de utilizar el objeto MongoDbManager para acceder a la base de datos de Mongo y a través
     * de la búsqueda de la coleccion Pedido, ejecutar un método que devuelve un DeleteResult el cual va a devolver
     * True si el cambio es conocido. Lanza una excepción en caso contrario.
     *
     * @param entity Objeto a borrar en la base de datos.
     *
     * @return Pedido?, el objeto introducido por parámetros, si no se encuentra, devolverá nulo.
     */
    override suspend fun delete(entity: Pedido): Boolean {
        logger.debug { "delete($entity) - borrando" }
        return MongoDbManager.database.getCollection<Pedido>()
            .deleteOneById(entity.id).wasAcknowledged()
    }

    suspend fun deleteAll() {
        logger.debug { "deleteAll() - borrando" }
        val ids = findAll().toList().map { it.id }.forEach {
            MongoDbManager.database.getCollection<Pedido>()
                .deleteOneById(it)}

    }
}