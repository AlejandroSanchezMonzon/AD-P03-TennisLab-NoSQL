/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.repositories.tareas

import es.dam.adp03_springmongodb.exceptions.RestException
import es.dam.adp03_springmongodb.mappers.toModelTarea
import es.dam.adp03_springmongodb.mappers.toTareaAPIDTO
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
class TareasRestRepository : CRUDRepository<Tarea, ObjectId> {
    private val client by lazy { KtorFitClient.instance }

    /**
     * Método encargado de utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve una lista de todos los objetos que hay
     * de tipo TareaAPI en la API REST.
     *
     * @throws RestException, cuando no ha sido posible devolver los objetos encontradas como un flujo.
     *
     * @return Flow<Tarea>, el flujo de objetos encontrados transfromados a modelo.
     */
    override suspend fun findAll(): Flow<Tarea> = withContext(Dispatchers.IO) {
        logger.info { "findAll()" }
        val call = client.getAllTareas()
        val tareas: MutableList<Tarea> = mutableListOf<Tarea>()

        try {
            logger.info { "findAll() - Realizado correctamente." }
            call.forEach {
                tareas.add(it.toModelTarea())
            }
            return@withContext tareas.asFlow()
        } catch (e: Exception) {
            logger.error { "findAll() - Error." }
            throw RestException("Error al obtener todas las tareas: ${e.message}")
        }
    }

    /**
     * Método encargado de utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto con el identificador dado que hay
     * de tipo TareaAPI en la API REST.
     *
     * @param id identificador de tipo String del objeto a consultar.
     *
     * @return Tarea?, el objeto que tiene el identificador introducido por parámetros, si no se encuentra, devolverá nulo.
     */
    override suspend fun findById(id: ObjectId): Tarea {
        logger.info { "finById(id=$id)" }
        val call = client.getTareaById(id)
        try {
            logger.info { "findById(id=$id) - Realizado correctamente." }
            return call.toModelTarea()
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - Error." }
            throw RestException("Error al obtener la tarea con id $id: ${e.message}")
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto a insertar
     * de tipo TareaAPI en la API REST.
     *
     * @param entity Objeto a insetar en la base de datos.
     *
     * @throws RestException, cuando no ha sido posible insertar el objeto.
     *
     * @return Tarea, el objeto que ha sido insertado.
     */
    override suspend fun save(entity: Tarea): Tarea {
        logger.info { "save(entity=$entity)" }
        try {
            val res = client.createTarea(entity.toTareaAPIDTO())
            logger.info { "save(entity=$entity) - Realizado correctamente." }
            return Tarea(
                id = ObjectId(res.id.toString().padStart(24, '0')),
                uuid = entity.uuid,
                precio = entity.precio,
                descripcion = res.title,
                tipo = entity.tipo,
                turno = entity.turno
            )
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - Error." }
            throw RestException("Error al crear la tarea ${entity.id}: ${e.message}")
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto a actualizar
     * de tipo TareaAPI en la API REST.
     *
     * @param entity Objeto a actualizar en la base de datos.
     *
     * @throws RestException, cuando no ha sido posible actualizar el objeto.
     *
     * @return Tarea, el objeto que ha sido actualizado o insertado.
     */
    override suspend fun update(entity: Tarea): Tarea {
        logger.info { "update(entity=$entity)" }
        try {
            val res = client.updateTarea(entity.id, entity.toTareaAPIDTO())
            logger.info { "update(entity=$entity) - Realizado correctamente." }
            return Tarea(
                id = ObjectId(res.id.toString().padStart(24, '0')),
                uuid = entity.uuid,
                precio = entity.precio,
                descripcion = res.title,
                tipo = entity.tipo,
                turno = entity.turno
            )
        } catch (e: RestException) {
            logger.error { "update(entity=$entity) - Error." }
            throw RestException("Error al actualizar la tarea con id ${entity.id}: ${e.message}")
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto a eliminar
     * de tipo TareaAPI en la API REST.
     *
     * @param entity Objeto a borrar en la base de datos.
     *
     * @throws RestException, cuando no ha sido posible borrar el objeto.
     *
     * @return Tarea, el objeto introducido por parámetros.
     */
    override suspend fun delete(entity: Tarea): Tarea {
        logger.info { "delete(entity=$entity)" }
        try {
            client.deleteTarea(entity.id)
            logger.info { "delete(entity=$entity) - Realizado correctamente." }
            return entity
        } catch (e: Exception) {
            logger.error { "delete(entity=$entity) - Error." }
            throw RestException("Error al eliminar la tarea con id ${entity.id}: ${e.message}")
        }
    }
}