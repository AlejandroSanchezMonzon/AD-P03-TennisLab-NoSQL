package repositories.usuarios

import exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mappers.toModelUsuario
import models.TipoUsuario
import models.Usuario
import mu.KotlinLogging
import services.ktorfit.KtorFitClient
import java.util.*

private val logger = KotlinLogging.logger {}

class UsuariosRestRepository: IUsuariosRepository {

    private val client by lazy { KtorFitClient.instance }

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

    override suspend fun findById(id: String): Usuario {
        logger.debug { "finById(id=$id)" }
        val call = client.getUsuarioById(id)
        try {
            logger.debug { "findById(id=$id) - Realizado correctamente." }
            return call.data!!
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - Error." }
            throw RestException("Error al obtener el usuario con id $id: ${e.message}")
        }
    }

    override suspend fun save(entity: Usuario): Usuario {
        logger.debug { "save(entity=$entity)" }
        try {
            val res = client.createUsuario(entity)
            logger.debug { "save(entity=$entity) - Realizado correctamente." }
            return Usuario(
                id = res.id,
                uuid = UUID.fromString(res.uuid),
                nombre = res.nombre,
                apellido = res.apellido,
                email = res.email,
                password = res.password,
                rol = TipoUsuario.valueOf(res.rol)
            )
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - Error." }
            throw RestException("Error al crear el usuario ${entity.id}: ${e.message}")
        }
    }

    override suspend fun update(entity: Usuario): Usuario {
        logger.debug { "update(entity=$entity)" }
        try {
            val res = client.updateUsuario(entity.id, entity)
            logger.debug { "update(entity=$entity) - Realizado correctamente." }
            return Usuario(
                id = res.id,
                uuid = UUID.fromString(res.uuid),
                nombre = res.nombre,
                apellido = res.apellido,
                email = res.email,
                password = res.password,
                rol = TipoUsuario.valueOf(res.rol)
            )
        } catch (e: RestException) {
            logger.error { "update(entity=$entity) - Error." }
            throw RestException("Error al actualizar el usuario con id ${entity.id}: ${e.message}")
        }
    }

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