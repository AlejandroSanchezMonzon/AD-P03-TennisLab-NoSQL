package es.dam.adp03_springmongodb.repositories.usuarios

import es.dam.adp03_springmongodb.models.TipoUsuario
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.services.sqldelight.SqlDeLightClient
import es.dam.adp03_springmongodb.utils.cifrarPassword
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UsuariosCacheRepositoryTest {
    private val usuariosRepository = UsuariosCacheRepository(SqlDeLightClient)

    private val usuario = Usuario(
        id = ObjectId("11".padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
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
    fun tearDown() = runTest {
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

        val res = usuariosRepository.findById(usuario.id.toString())

        assert(res == usuario)
    }

    @Test
    fun findByIdNoExiste() = runTest {
        val res = usuariosRepository.findById(usuario.id.toString())

        assert(res == null)
    }

    @Test
    fun save() = runTest {
        val res = usuariosRepository.save(usuario)
        usuariosRepository.findAll().first().toList().forEach { println("all" + it) }

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
                id = ObjectId("1".padStart(24, '0')),
                uuid = UUID.randomUUID().toString(),
                nombre = "actualizado",
                apellido = "actualizado",
                email = "actualizado",
                password = cifrarPassword("James"),
                rol = TipoUsuario.ENCORDADOR
            )
        )

        assertAll(
            { assertEquals(res.id, ObjectId("1".padStart(24, '0'))) },
            { assertEquals(res.nombre, "actualizado") },
            { assertEquals(res.apellido, "actualizado") },
            { assertEquals(res.email, "actualizado") },
        )
    }

    @Test
    fun delete() = runTest {
        usuariosRepository.save(usuario)

        val res = usuariosRepository.delete(usuario)

        assert(res == usuario)
    }

    @Test
    fun deleteNoExiste() = runTest {
        usuariosRepository.findAll().first().toList().forEach { "all" + println(it) }
        usuariosRepository.findById(usuario.id.toString())
        val res = usuariosRepository.delete(usuario)

        assert(res == null)
    }
}