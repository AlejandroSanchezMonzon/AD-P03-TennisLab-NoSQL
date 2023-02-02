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

    override suspend fun findById(id: ObjectId): Usuario {
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

    override suspend fun save(entity: Usuario): Usuario {
        logger.debug { "save(entity=$entity)" }
        try {
            val res = client.createUsuario(entity.toUsuarioAPIDTO())
            logger.debug { "save(entity=$entity) - Realizado correctamente." }
            return res.toModelUsuario()
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - Error." }
            throw RestException("Error al crear el usuario ${entity.id}: ${e.message}")
        }
    }

    override suspend fun update(entity: Usuario): Usuario {
        logger.debug { "update(entity=$entity)" }
        try {
            val res = client.updateUsuario(entity.id, entity.toUsuarioAPIDTO())
            logger.debug { "update(entity=$entity) - Realizado correctamente." }
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