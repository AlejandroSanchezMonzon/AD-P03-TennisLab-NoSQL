package es.dam.adp03_springmongodb.repositories.tareas

import es.dam.adp03_springmongodb.exceptions.RestException
import es.dam.adp03_springmongodb.mappers.toModelTarea
import es.dam.adp03_springmongodb.models.Tarea
import es.dam.adp03_springmongodb.repositories.CRUDRepository
import es.dam.adp03_springmongodb.services.ktorfit.KtorFitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

private val logger = KotlinLogging.logger {}

@Repository
class TareasRestRepository: CRUDRepository<Tarea, ObjectId> {
    private val client by lazy { KtorFitClient.instance }

    override suspend fun findAll(): Flow<Tarea> = withContext(Dispatchers.IO) {
        logger.debug { "findAll()" }
        val call = client.getAllTareas()
        val tareas = mutableListOf<Tarea>()

        call.forEach {
            tareas.add(it.toModelTarea())
        }

        try {
            logger.debug { "findAll() - Realizado correctamente." }
            return@withContext tareas.asFlow()
        } catch (e: Exception) {
            logger.error { "findAll() - Error." }
            throw RestException("Error al obtener todas las tareas: ${e.message}")
        }
    }

    override suspend fun findById(id: ObjectId): Tarea {
        logger.debug { "finById(id=$id)" }
        val call = client.getTareaById(id)
        try {
            logger.debug { "findById(id=$id) - Realizado correctamente." }
            return call.toModelTarea()
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - Error." }
            throw RestException("Error al obtener la tarea con id $id: ${e.message}")
        }
    }

    override suspend fun save(entity: Tarea): Tarea {
        logger.debug { "save(entity=$entity)" }
        try {
            val res = client.createTarea(entity)
            logger.debug { "save(entity=$entity) - Realizado correctamente." }
            return res.toModelTarea()
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - Error." }
            throw RestException("Error al crear la tarea ${entity.id}: ${e.message}")
        }    }

    override suspend fun update(entity: Tarea): Tarea {
        logger.debug { "update(entity=$entity)" }
        try {
            val res = client.updateTarea(entity.id, entity)
            logger.debug { "update(entity=$entity) - Realizado correctamente." }
            return Tarea(
                id = entity.id,
                uuid = entity.uuid,
                precio = entity.precio,
                descripcion = entity.descripcion,
                tipo = entity.tipo,
                turno = entity.turno
            )
        } catch (e: RestException) {
            logger.error { "update(entity=$entity) - Error." }
            throw RestException("Error al actualizar la tarea con id ${entity.id}: ${e.message}")
        }
    }

    override suspend fun delete(entity: Tarea): Tarea {
        logger.debug { "delete(entity=$entity)" }
        try {
            client.deleteTarea(entity.id)
            logger.debug { "delete(entity=$entity) - Realizado correctamente." }
            return entity
        } catch (e: Exception) {
            logger.error { "delete(entity=$entity) - Error." }
            throw RestException("Error al eliminar la tarea con id ${entity.id}: ${e.message}")
        }
    }
}