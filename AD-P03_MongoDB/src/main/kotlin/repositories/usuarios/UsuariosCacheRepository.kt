/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.usuarios

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mappers.toModel
import mappers.toModelUsuario
import mappers.toUsuarioAPIDTO
import models.Usuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import services.ktorfit.KtorFitClient
import services.sqldelight.SqlDeLightClient
import java.util.*

private val logger = KotlinLogging.logger {}
private const val COOLDOWN = 6 * 10000L

@Single
@Named("UsuariosCacheRepository")
class UsuariosCacheRepository(
    @Named("SqlDeLightClient")
    cliente: SqlDeLightClient
) {
    private val remote = KtorFitClient.instance
    private val cache = cliente.queries

    suspend fun refresh() = withContext(Dispatchers.IO) {
        launch {
            do {
                logger.debug { "Refrescando los datos de la cache..." }
                cache.removeAllUsuarios()
                remote.getAllUsuarios().forEach { usuarioAPI ->
                    val usuario = usuarioAPI.toModelUsuario()
                    cache.createUsuario(
                        usuario.id.toLong(),
                        usuario.uuid.toString(),
                        usuario.nombre,
                        usuario.apellido,
                        usuario.email,
                        usuario.password,
                        usuario.rol.toString()
                    )
                }
                delay(COOLDOWN)
            } while (true)
        }
    }

    /**
     * Método encargado de utilizar el cliente SqlDeLightClient para acceder a la base de datos creada a
     * través de fichero .sq, ejecuta un método, definido también en el fichero mencionado, que devuelve
     * una query de Usuario de todos los objetos que hay.
     *
     * @return Flow<List<Usuario>>, el flujo de la lista de objetos encontrados transfromados a modelo.
     */
    fun findAll(): Flow<List<Usuario>> {
        logger.debug { "Cache -> findAll() " }

        return cache.selectUsuarios().asFlow().mapToList()
            .map { it.map { usuario -> usuario.toModel() } }
    }

    /**
     * Método encargado de utilizar el cliente SqlDeLightClient para acceder a la base de datos creada a
     * través de fichero .sq, ejecuta un método, definido también en el fichero mencionado, que devuelve
     * una query del Usuario cuyo identificador es el dado por parámetros.
     *
     * @param id identificador de tipo Flot del objeto a consultar.
     *
     * @return Usuario, el objeto que tiene el identificador introducido por parámetros transfromado a modelo.
     */
    fun findById(id: Long): Usuario? {
        logger.debug { "Cache -> findById($id)" }
        return try {
            cache.selectUsuarioById(id).executeAsOne().toModel()
        } catch (e: Exception) {
            logger.error { "Usuario no encontrado." }
            null
        }
    }

    /**
     * Método encargado de utilizar el cliente SqlDeLightClient para acceder a la base de datos creada a
     * través de fichero .sq, ejecuta un método, definido también en el fichero mencionado, el cual
     * inserta el Usuario dado por parámetros.
     *
     * Antes de hacer la inserción a la caché se encarga de crear el usuario en remoto para que no
     * haya inconsistencia de datos.
     *
     * @param entity Objeto a insetar en la base de datos.
     *
     * @return Usuario, el objeto que ha sido insertado.
     */
    suspend fun save(entity: Usuario): Usuario {
        logger.debug { "Cache -> save($entity)" }
        val dto = remote.createUsuario(entity.toUsuarioAPIDTO())
        val usuario = dto.toModelUsuario()

        cache.createUsuario(
            usuario.id.toLong(),
            usuario.uuid.toString(),
            usuario.nombre,
            usuario.apellido,
            usuario.email,
            usuario.password,
            usuario.rol.toString()
        )
        return usuario
    }

    /**
     * Método encargado de utilizar el cliente SqlDeLightClient para acceder a la base de datos creada a
     * través de fichero .sq, ejecuta un método, definido también en el fichero mencionado, el cual
     * actualiza los valores del Usuario cuyo identificador es el mismo que el dado por parámetros.
     *
     * Después de esta operación se encarga de actualizar el usuario en remoto para que no haya ninguna
     * inconsistencia de datos.
     *
     * @param entity Objeto a actualizar en la base de datos.
     **
     * @return Usuario, el objeto que ha sido actualizado.
     */
    suspend fun update(entity: Usuario): Usuario {
        logger.debug { "Cache -> update($entity)" }
        cache.updateUsuario(
            id = entity.id.toLong(),
            uuid = entity.uuid.toString(),
            nombre = entity.nombre,
            apellido = entity.apellido,
            email = entity.email,
            password = entity.password,
            rol = entity.rol.toString()
        )

        val dto = remote.updateUsuario(entity.id, entity.toUsuarioAPIDTO())

        return dto.toModelUsuario()
    }

    /**
     * Método encargado de utilizar el cliente SqlDeLightClient para acceder a la base de datos creada a
     * través de fichero .sq, ejecuta un método, definido también en el fichero mencionado, el cual
     * borra el Usuario dado por parámetros.
     *
     * Después de esta operación se encarga de borrar el usuario en remoto para que no haya ninguna
     * inconsistencia de datos.
     *
     * @param entity Objeto a borrar en la base de datos.
     *
     * @return Usuario, el objeto introducido por parámetros.
     */
    suspend fun delete(entity: Usuario): Usuario {
        logger.debug { "Cache -> delete($entity)" }

        cache.deleteUsuario(entity.id.toLong())
        remote.deleteUsuario(entity.id)

        return entity
    }
}