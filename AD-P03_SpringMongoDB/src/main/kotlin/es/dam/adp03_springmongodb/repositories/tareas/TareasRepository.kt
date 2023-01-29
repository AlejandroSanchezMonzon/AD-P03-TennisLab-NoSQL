package es.dam.adp03_springmongodb.repositories.tareas

import db.MongoDbManager
import exceptions.DataBaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import models.Tarea
import mu.KotlinLogging
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

class TareasRepository: ITareasRepository {
    override suspend fun findAll(): Flow<Tarea> {
        logger.debug { "findAll()" }
        return MongoDbManager.database.getCollection<Tarea>().find().asFlow()      }

    override suspend fun findById(id: String): Tarea {
        logger.debug { "findById($id)" }
        return (MongoDbManager.database.getCollection<Tarea>()
            .findOneById(id) ?: throw DataBaseException("No existe la tarea con id $id"))    }

    override suspend fun save(entity: Tarea): Tarea {
        logger.debug { "save($entity) - guardando" }
        return MongoDbManager.database.getCollection<Tarea>()
            .save(entity).let { entity }    }

    override suspend fun update(entity: Tarea): Tarea {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Tarea>()
            .save(entity).let { entity }      }

    override suspend fun delete(entity: Tarea): Tarea {
        logger.debug { "delete($entity) - borrando" }
        return MongoDbManager.database.getCollection<Tarea>()
            .deleteOneById(entity.id).let { entity }    }
}