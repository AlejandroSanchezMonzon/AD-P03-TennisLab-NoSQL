package repositories.usuarios

import kotlinx.coroutines.flow.toList
import models.TipoUsuario
import models.Usuario
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import utils.cifrarPassword
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UsuariosRestRepositoryTest {
    private val usuariosRepository = UsuariosRestRepository()

    private val usuario = Usuario(
        id = "1",
        uuid = UUID.randomUUID(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.ENCORDADOR
    )

    @Test
    suspend fun findAll() {
        val res = usuariosRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
    suspend fun findById() {
        assertThrows<RuntimeException> {
            usuariosRepository.findById(usuario.id)
        }

    }

    @Test
    suspend fun save() {
        val res = usuariosRepository.save(usuario)

        assertAll(
            { assertEquals(res.id, usuario.id) },
            { assertEquals(res.uuid, usuario.uuid) },
            { assertEquals(res.nombre, usuario.nombre) },
            { assertEquals(res.apellido, usuario.apellido) },
            { assertEquals(res.email, usuario.email) },
            { assertEquals(res.rol, usuario.rol) },
        )

        usuariosRepository.delete(usuario)
    }

    @Test
    suspend fun update() {
        usuariosRepository.save(usuario)
        val operacion = usuariosRepository.update(
            Usuario(
                id = "1",
                uuid = UUID.randomUUID(),
                nombre = "actualizado",
                apellido = "actualizado",
                email = "actualizado",
                password = cifrarPassword("James"),
                rol = TipoUsuario.ENCORDADOR
            )
        )
        val res = usuariosRepository.findById(operacion.id)

        assertAll(
            { assertEquals(res.id, operacion.id) },
            { assertEquals(res.uuid, operacion.uuid) },
            { assertEquals(res.nombre, operacion.nombre) },
            { assertEquals(res.apellido, operacion.apellido) },
            { assertEquals(res.email, operacion.email) },

            )

        usuariosRepository.delete(usuario)
    }

    @Test
    suspend fun updateNoExiste() {
        assertThrows<RuntimeException> {
            usuariosRepository.update(
                Usuario(
                    id = "98",
                    uuid = UUID.randomUUID(),
                    nombre = "actualizado",
                    apellido = "actualizado",
                    email = "actualizado",
                    password = cifrarPassword("James"),
                    rol = TipoUsuario.ENCORDADOR
                )
            )
        }
    }

    @Test
    suspend fun delete() {
        usuariosRepository.save(usuario)

        val res = usuariosRepository.delete(usuario)

        assert(res==usuario)
    }

    @Test
    suspend fun deleteNoExiste() {
        assertThrows<RuntimeException> {
            usuariosRepository.delete(usuario)
        }

    }
}