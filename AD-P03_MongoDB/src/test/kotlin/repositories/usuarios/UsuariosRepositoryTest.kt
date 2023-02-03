package repositories.usuarios

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import models.TipoUsuario
import models.Usuario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import utils.cifrarPassword
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UsuariosRepositoryTest {
    private val usuariosRepository = UsuariosRepository()

    private val usuario = Usuario(
        id = "1",
        uuid = UUID.randomUUID(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.ENCORDADOR
    )

    init {
        MockKAnnotations.init(this)
    }

    @BeforeAll
    fun setUp() = runTest {
        usuariosRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() = runTest{
        usuariosRepository.deleteAll()
    }

    @Test
     fun findAll()  = runTest{
        val res = usuariosRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
     fun findById()  = runTest{
        usuariosRepository.save(usuario)

        val res = usuariosRepository.findById(usuario.id)

        assert(res == usuario)
    }

    @Test
     fun findByIdNoExiste() = runTest {
        val res = usuariosRepository.findById("-5")

        assert(res == null)
    }

    @Test
     fun save() = runTest {
        val res = usuariosRepository.save(usuario)

        assertAll(
            { assertEquals(res.id, usuario.id) },
            { assertEquals(res.uuid, usuario.uuid) },
            { assertEquals(res.nombre, usuario.nombre) },
            { assertEquals(res.apellido, usuario.apellido) },
            { assertEquals(res.email, usuario.email) },
            { assertEquals(res.rol, usuario.rol) },
        )
    }

    @Test
     fun update() = runTest {
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
            { assertEquals(res?.id, operacion.id) },
            { assertEquals(res?.uuid, operacion.uuid) },
            { assertEquals(res?.nombre, operacion.nombre) },
            { assertEquals(res?.apellido, operacion.apellido) },
            { assertEquals(res?.email, operacion.email) },

        )
    }

    @Test
     fun delete() = runTest {
        usuariosRepository.save(usuario)

        val res = usuariosRepository.delete(usuario)

        assert(res)
    }

    @Test
     fun deleteNoExiste()  = runTest{
        val res = usuariosRepository.delete(usuario)

        assert(!res)
    }
}