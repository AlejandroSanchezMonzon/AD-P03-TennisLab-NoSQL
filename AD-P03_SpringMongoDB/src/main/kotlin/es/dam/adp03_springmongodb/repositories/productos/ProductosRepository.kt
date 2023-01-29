package es.dam.adp03_springmongodb.repositories.productos

import db.MongoDbManager
import exceptions.DataBaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import models.Producto
import mu.KotlinLogging
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

class ProductosRepository: IProductosRepository {
    override suspend fun findAll(): Flow<Producto> {
        logger.debug { "findAll()" }

        return MongoDbManager.database.getCollection<Producto>().find().asFlow()
    }

    override suspend fun findById(id: String): Producto {
        logger.debug { "findById($id)" }

        return (MongoDbManager.database.getCollection<Producto>()
            .findOneById(id) ?: throw DataBaseException("No existe el producto con id $id"))
    }

    override suspend fun save(entity: Producto): Producto {
        logger.debug { "save($entity) - guardando" }

        return MongoDbManager.database.getCollection<Producto>()
            .save(entity).let { entity }
    }

    override suspend fun update(entity: Producto): Producto {
        logger.debug { "save($entity) - actualizando" }

        return MongoDbManager.database.getCollection<Producto>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Producto): Producto {
        logger.debug { "delete($entity) - borrando" }

        return MongoDbManager.database.getCollection<Producto>()
            .deleteOneById(entity.id).let { entity }
    }
}