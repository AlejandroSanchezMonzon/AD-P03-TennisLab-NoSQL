package es.dam.adp03_springmongodb.repositories.maquinas

import db.MongoDbManager
import exceptions.DataBaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import models.Maquina
import mu.KotlinLogging
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

class MaquinasRepository: IMaquinasRepository {
    override suspend fun findAll(): Flow<Maquina> {
        logger.debug { "findAll()" }
        return MongoDbManager.database.getCollection<Maquina>().find().asFlow()        }

    override suspend fun findById(id: String): Maquina {
        logger.debug { "findById($id)" }
        return (MongoDbManager.database.getCollection<Maquina>()
            .findOneById(id) ?: throw DataBaseException("No existe la máquina con id $id"))    }

    override suspend fun save(entity: Maquina): Maquina {
        logger.debug { "save($entity) - guardando" }
        return MongoDbManager.database.getCollection<Maquina>()
            .save(entity).let { entity }
    }

    override suspend fun update(entity: Maquina): Maquina {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Maquina>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Maquina): Maquina {
        logger.debug { "delete($entity) - borrando" }
        return MongoDbManager.database.getCollection<Maquina>()
            .deleteOneById(entity.id).let { entity }
    }
}