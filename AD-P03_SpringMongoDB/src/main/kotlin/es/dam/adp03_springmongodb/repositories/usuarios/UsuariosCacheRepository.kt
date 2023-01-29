package es.dam.adp03_springmongodb.repositories.usuarios

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mappers.toModel
import models.TipoUsuario
import models.Usuario
import mu.KotlinLogging
import services.ktorfit.KtorFitClient
import services.sqldelight.SqlDeLightClient
import java.util.*


private val logger = KotlinLogging.logger {}
private const val COOLDOWN = 6 * 10000L
class UsuariosCacheRepository(cliente: SqlDeLightClient) {
    private val remote = KtorFitClient.instance
    private val cache = cliente.queries

    suspend fun refresh() = withContext(Dispatchers.IO) {
        launch {
            do {
                logger.debug { "Refrescando los datos de la cache..." }
                cache.removeAllUsuarios()
                remote.getAllUsuarios().data?.forEach { usuario ->
                    cache.createUsuario(usuario.id.toLong(), usuario.uuid.toString(), usuario.nombre, usuario.apellido, usuario.email, usuario.password, usuario.rol.toString())
                }
                delay(COOLDOWN)
            } while (true)
        }
    }

    fun findAll(): Flow<List<Usuario>> {
        logger.debug { "Cache -> findAll() " }

        return cache.selectUsuarios().asFlow().mapToList()
            .map { it.map { usuario -> usuario.toModel() } }
    }

    fun findById(id: Long): Usuario {
        logger.debug { "Cache -> findById($id)" }

        return cache.selectUsuarioById(id).executeAsOne().toModel()
    }

    suspend fun save(entity: Usuario): Usuario {
        logger.debug { "Cache -> save($entity)" }
        val dto = remote.createUsuario(entity)
        val usuario = Usuario(
            id = dto.id,
            uuid = UUID.fromString(dto.uuid),
            nombre = dto.nombre,
            apellido = dto.apellido,
            email = dto.email,
            password = dto.password,
            rol = TipoUsuario.valueOf(dto.rol)
        )

        cache.createUsuario(usuario.id.toLong(), usuario.uuid.toString(), usuario.nombre, usuario.apellido, usuario.email, usuario.password, usuario.rol.toString())
        return usuario
    }

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

        val dto = remote.updateUsuario(entity.id, entity)

        return Usuario(
            id = dto.id,
            uuid = UUID.fromString(dto.uuid),
            nombre = dto.nombre,
            apellido = dto.apellido,
            email = dto.email,
            password = dto.password,
            rol = TipoUsuario.valueOf(dto.rol)
        )
    }

    suspend fun delete(entity: Usuario): Usuario {
        logger.debug { "Cache -> delete($entity)" }

        cache.deleteUsuario(entity.id.toLong())
        remote.deleteUsuario(entity.id)

        return entity
    }
}