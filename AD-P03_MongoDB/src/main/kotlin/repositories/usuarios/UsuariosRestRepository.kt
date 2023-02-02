/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.usuarios

import exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mappers.toModelUsuario
import models.Usuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import services.ktorfit.KtorFitClient
import utils.cifrarPassword
import java.util.*

private val logger = KotlinLogging.logger {}

@Single
@Named("UsuariosRestRepository")
class UsuariosRestRepository: IUsuariosRepository {

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
        logger.debug { "findAll()" }
        val call = client.getAllUsuarios()
        val usuarios = mutableListOf<Usuario>()

        call.forEach {
            usuarios.add(it.toModelUsuario())
        }

        try {
            logger.debug { "findAll() - Realizado correctamente." }
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
    override suspend fun findById(id: String): Usuario {
        logger.debug { "finById(id=$id)" }
        val call = client.getUsuarioById(id)
        try {
            logger.debug { "findById(id=$id) - Realizado correctamente." }
            return call.toModelUsuario()
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - Error." }
            throw RestException("Error al obtener el usuario con id $id: ${e.message}")
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
        logger.debug { "save(entity=$entity)" }
        try {
            val res = client.createUsuario(entity)
            logger.debug { "save(entity=$entity) - Realizado correctamente." }
            return Usuario(
                id = res.id.toString(),
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
        logger.debug { "update(entity=$entity)" }
        try {
            val res = client.updateUsuario(entity.id, entity)
            logger.debug { "update(entity=$entity) - Realizado correctamente." }
            return Usuario(
                id = res.id.toString(),
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
     * @return Usuario, el objeto introducido por parámetros.
     */
    override suspend fun delete(entity: Usuario): Usuario {
        logger.debug { "delete(entity=$entity)" }
        try {
            client.deleteUsuario(entity.id)
            logger.debug { "delete(entity=$entity) - Realizado correctamente." }
            return entity
        } catch (e: Exception) {
            logger.error { "delete(entity=$entity) - Error." }
            throw RestException("Error al eliminar el usuario con id ${entity.id}: ${e.message}")
        }
    }
}