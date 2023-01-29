package repositories.tareas

import exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import models.Tarea
import models.TipoTarea
import mu.KotlinLogging
import services.ktorfit.KtorFitClient
import java.util.*

private val logger = KotlinLogging.logger {}


class TareasRestRepository: ITareasRepository {

    private val client by lazy { KtorFitClient.instance }

    override suspend fun findAll(): Flow<Tarea> = withContext(Dispatchers.IO) {
        logger.debug { "findAll()" }
        val call = client.getAllTareas()
        try {
            logger.debug { "findAll() - Realizado correctamente." }
            return@withContext call.data!!.asFlow()
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
            return call.data!!
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
            return Tarea(
                id = res.id,
                uuid = UUID.fromString(res.uuid),
                precio = res.precio,
                descripcion = res.descripcion,
                tipo = TipoTarea.valueOf(res.tipo),
                turno = res.turno
            )
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
                id = res.id,
                uuid = UUID.fromString(res.uuid),
                precio = res.precio,
                descripcion = res.descripcion,
                tipo = TipoTarea.valueOf(res.tipo),
                turno = res.turno
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