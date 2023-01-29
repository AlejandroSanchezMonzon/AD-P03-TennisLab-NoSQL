package es.dam.adp03_springmongodb.repositories.usuarios

import db.MongoDbManager
import exceptions.DataBaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import models.Usuario
import mu.KotlinLogging
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

class UsuariosRepository: IUsuariosRepository {
    override suspend fun findAll(): Flow<Usuario> {
        logger.debug { "findAll()" }
        return MongoDbManager.database.getCollection<Usuario>().find().asFlow()
    }

    override suspend fun findById(id: String): Usuario {
        logger.debug { "findById($id)" }
        return (MongoDbManager.database.getCollection<Usuario>()
            .findOneById(id) ?: throw DataBaseException("No existe el usuario con id $id"))
    }

    override suspend fun save(entity: Usuario): Usuario {
        logger.debug { "save($entity) - guardando" }
        return MongoDbManager.database.getCollection<Usuario>()
            .save(entity).let { entity }
    }

    override suspend fun update(entity: Usuario): Usuario {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Usuario>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Usuario): Usuario {
        logger.debug { "delete($entity) - borrando" }
        return MongoDbManager.database.getCollection<Usuario>()
            .deleteOneById(entity.id).let { entity }
    }
}