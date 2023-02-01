package repositories.tareas

import db.getTurnosInit
import exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mappers.toModelTarea
import mappers.toModelUsuario
import mappers.toTareaAPIDTO
import models.Tarea
import models.TipoTarea
import models.Usuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import services.ktorfit.KtorFitClient
import utils.randomTareaType
import java.util.*

private val logger = KotlinLogging.logger {}

@Single
@Named("TareasRestRepository")
class TareasRestRepository: ITareasRepository {

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

    override suspend fun findById(id: String): Tarea {
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
            val res = client.createTarea(entity.toTareaAPIDTO())
            logger.debug { "save(entity=$entity) - Realizado correctamente." }
            return res.toModelTarea()
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - Error." }
            throw RestException("Error al crear la tarea ${entity.id}: ${e.message}")
        }    }

    override suspend fun update(entity: Tarea): Tarea {
        logger.debug { "update(entity=$entity)" }
        try {
            val res = client.updateTarea(entity.id, entity.toTareaAPIDTO())
            logger.debug { "update(entity=$entity) - Realizado correctamente." }
            return res.toModelTarea()
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