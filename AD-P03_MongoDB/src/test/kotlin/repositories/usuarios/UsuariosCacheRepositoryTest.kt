package repositories.usuarios

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import models.TipoUsuario
import models.Usuario
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import services.sqldelight.SqlDeLightClient
import utils.cifrarPassword
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UsuariosCacheRepositoryTest {
    private val usuariosRepository = UsuariosCacheRepository(SqlDeLightClient())

    private val usuario = Usuario(
        id = "11",
        uuid = UUID.randomUUID(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.TENISTA
    )

    init {
        MockKAnnotations.init(this)
    }

    @BeforeEach
    fun setUp() = runTest {
        usuariosRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() = runTest{
        usuariosRepository.deleteAll()
    }

    @Test
     fun findAll() = runTest {
        val res = usuariosRepository.findAll().first().toList()

        assert(res.isEmpty())
    }

    @Test
     fun findById() = runTest {
        usuariosRepository.save(usuario)

        val res = usuariosRepository.findById(usuario.id.toLong())

        assert(res == usuario)
    }

    @Test
    fun findByIdNoExiste() = runTest {
        val res = usuariosRepository.findById(usuario.id.toLong())

        assert(res == null)
    }

    @Test
     fun save() = runTest {
        val res = usuariosRepository.save(usuario)
        usuariosRepository.findAll().first().toList().forEach { println("all" + it)}

        assertAll(
            { assertEquals(res.id, usuario.id) },
            { assertEquals(res.uuid, usuario.uuid) },
            { assertEquals(res.nombre, usuario.nombre) },
            { assertEquals(res.apellido, usuario.apellido) },
            { assertEquals(res.email, usuario.email) },
            { assertEquals(res.rol, usuario.rol) }
        )
     }

    @Test
     fun update() = runTest {
        usuariosRepository.save(usuario)
        val res = usuariosRepository.update(
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

        assertAll(
            { assertEquals(res?.id, "1") },
            { assertEquals(res?.nombre, "actualizado") },
            { assertEquals(res?.apellido, "actualizado") },
            { assertEquals(res?.email,"actualizado") },
        )
    }

    @Test
     fun delete() = runTest {
        usuariosRepository.save(usuario)

        val res = usuariosRepository.delete(usuario)

        assert(res==usuario)
    }

    @Test
    fun deleteNoExiste() = runTest {
        val d = usuariosRepository.findAll().first().toList().forEach { "all" + println(it) }
        val preuba = usuariosRepository.findById(usuario.id.toLong())
        val res = usuariosRepository.delete(usuario)

        println(res)
        assert(res==null)
    }

}