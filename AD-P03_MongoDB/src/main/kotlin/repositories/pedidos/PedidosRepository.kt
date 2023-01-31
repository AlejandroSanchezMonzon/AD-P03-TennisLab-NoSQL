package repositories.pedidos

import db.MongoDbManager
import exceptions.DataBaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
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
    override suspend fun findAll(): Flow<Pedido> {
        logger.debug { "findAll()" }
        return MongoDbManager.database.getCollection<Pedido>().find().asFlow()
    }

    override suspend fun findById(id: String): Pedido {
        logger.debug { "findById($id)" }
        return (MongoDbManager.database.getCollection<Pedido>()
            .findOneById(id) ?: throw DataBaseException("No existe el pedido con id $id"))
    }

    override suspend fun save(entity: Pedido): Pedido {
        logger.debug { "save($entity) - guardando" }
        return MongoDbManager.database.getCollection<Pedido>()
            .save(entity).let { entity }    }

    override suspend fun update(entity: Pedido): Pedido {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Pedido>()
            .save(entity).let { entity }    }

    override suspend fun delete(entity: Pedido): Pedido {
        logger.debug { "delete($entity) - borrando" }
        return MongoDbManager.database.getCollection<Pedido>()
            .deleteOneById(entity.id).let { entity }
    }
}