package repositories.turnos

import db.MongoDbManager
import exceptions.DataBaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import models.Turno
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

@Single
@Named("TurnosRepository")
class TurnosRepository: ITurnosRepository {
    override suspend fun findAll(): Flow<Turno> {
        logger.debug { "findAll()" }
        return MongoDbManager.database.getCollection<Turno>().find().asFlow()
    }

    override suspend fun findById(id: String): Turno {
        logger.debug { "findById($id)" }
        return (MongoDbManager.database.getCollection<Turno>()
            .findOneById(id) ?: throw DataBaseException("No existe el turno con id $id"))    }

    override suspend fun save(entity: Turno): Turno {
        logger.debug { "save($entity) - guardando" }
        return MongoDbManager.database.getCollection<Turno>()
            .save(entity).let { entity }    }

    override suspend fun update(entity: Turno): Turno {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Turno>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Turno): Turno {
        logger.debug { "delete($entity) - borrando" }
        return MongoDbManager.database.getCollection<Turno>()
            .deleteOneById(entity.id).let { entity }
    }
}