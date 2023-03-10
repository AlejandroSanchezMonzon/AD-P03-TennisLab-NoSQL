/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.repositories.usuarios

import es.dam.adp03_springmongodb.exceptions.RestException
import es.dam.adp03_springmongodb.mappers.toModelUsuario
import es.dam.adp03_springmongodb.mappers.toUsuarioAPIDTO
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.repositories.CRUDRepository
import es.dam.adp03_springmongodb.services.ktorfit.KtorFitClient
import es.dam.adp03_springmongodb.utils.cifrarPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

private val logger = KotlinLogging.logger {}

@Repository
class UsuariosRestRepository : CRUDRepository<Usuario, ObjectId> {

    private val client by lazy { KtorFitClient.instance }

    /**
     * Método encargado de utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve una lista de todos los objetos que hay
     * de tipo UsuarioAPI en la API REST.
     *
     * @throws RestException, cuando no ha sido posible devolver los objetos encontradas como un flujo.
     *
     * @return Flow<Usuario>, el flujo de objetos encontrados transfromados a modelo.
     */
    override suspend fun findAll(): Flow<Usuario> = withContext(Dispatchers.IO) {
        logger.info { "findAll()" }
        val call = client.getAllUsuarios()
        val usuarios = mutableListOf<Usuario>()

        call.forEach {
            usuarios.add(it.toModelUsuario())
        }

        try {
            logger.info { "findAll() - Realizado correctamente." }

            return@withContext usuarios.asFlow()
        } catch (e: Exception) {
            logger.error { "findAll() - Error." }
            throw RestException("Error al obtener todos los usuarios: ${e.message}")
        }
    }

    /**
     * Método encargado de utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto con el identificador dado que hay
     * de tipo UsuarioAPI en la API REST.
     *
     * @param id identificador de tipo String del objeto a consultar.
     *
     * @return Usuario?, el objeto que tiene el identificador introducido por parámetros, si no se encuentra, devolverá nulo.
     */
    override suspend fun findById(id: ObjectId): Usuario? {
        logger.info { "finById(id=$id)" }
        val call = client.getUsuarioById(id)
        return try {
            logger.info { "findById(id=$id) - Realizado correctamente." }
            call.toModelUsuario()
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - Error." }
            null
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto a insertar
     * de tipo UsuarioAPI en la API REST.
     *
     * @param entity Objeto a insetar en la base de datos.
     *
     * @throws RestException, cuando no ha sido posible insertar el objeto.
     *
     * @return Usuario, el objeto que ha sido insertado.
     */
    override suspend fun save(entity: Usuario): Usuario {
        logger.info { "save(entity=$entity)" }
        try {
            val res = client.createUsuario(entity.toUsuarioAPIDTO())
            logger.info { "save(entity=$entity) - Realizado correctamente." }
            return Usuario(
                id = entity.id,
                uuid = entity.uuid,
                nombre = res.name,
                apellido = res.username,
                email = res.email,
                password = cifrarPassword(entity.password),
                rol = entity.rol
            )
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - Error." }
            throw RestException("Error al crear el usuario ${entity.id}: ${e.message}")
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto a actualizar
     * de tipo UsuarioAPI en la API REST.
     *
     * @param entity Objeto a actualizar en la base de datos.
     *
     * @throws RestException, cuando no ha sido posible actualizar el objeto.
     *
     * @return Usuario, el objeto que ha sido actualizado o insertado.
     */
    override suspend fun update(entity: Usuario): Usuario {
        logger.info { "update(entity=$entity)" }
        try {
            val res = client.updateUsuario(entity.id, entity.toUsuarioAPIDTO())
            logger.info { "update(entity=$entity) - Realizado correctamente." }
            return Usuario(
                id = ObjectId(res.id.toString().padStart(24, '0')),
                uuid = entity.uuid,
                nombre = res.name,
                apellido = res.username,
                email = res.email,
                password = cifrarPassword(entity.password),
                rol = entity.rol
            )
        } catch (e: RestException) {
            logger.error { "update(entity=$entity) - Error." }
            throw RestException("Error al actualizar el usuario con id ${entity.id}: ${e.message}")
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve el objeto a eliminar
     * de tipo UsuarioAPI en la API REST.
     *
     * @param entity Objeto a borrar en la base de datos.
     *
     * @throws RestException, cuando no ha sido posible borrar el objeto.
     *
     * @return Boolean, tue si se ha podido realizar la operación, false si no.
     */
    override suspend fun delete(entity: Usuario): Boolean {
        logger.info { "delete(entity=$entity)" }
        try {
            client.deleteUsuario(entity.id)
            logger.info { "delete(entity=$entity) - Realizado correctamente." }
            return true
        } catch (e: Exception) {
            logger.error { "delete(entity=$entity) - Error." }
            throw RestException("Error al eliminar el usuario con id ${entity.id}: ${e.message}")
        }
    }

    /**
     * Método encargadode utilizar una instancia del objeto KtorfitClient para acceder a la API y a través
     * de la interfaz KtorfitRest, ejecutar un método que devuelve se encarga de eliminar todos los usuarios.
     */
    suspend fun deleteAll() {
        logger.debug { "deleteAll()" }
        try {
            client.deleteAllUsuarios()
            logger.debug { "deleteAll() - Realizado correctamente." }
        } catch (e: Exception) {
            logger.error { "deleteAll() - Error." }
            throw RestException("Error al eliminar los usuarios: ${e.message}")
        }
    }
}