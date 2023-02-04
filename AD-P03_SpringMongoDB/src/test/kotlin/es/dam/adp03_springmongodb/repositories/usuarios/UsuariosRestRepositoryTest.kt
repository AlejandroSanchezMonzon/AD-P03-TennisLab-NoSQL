package es.dam.adp03_springmongodb.repositories.usuarios

import com.mongodb.assertions.Assertions.assertNotNull
import es.dam.adp03_springmongodb.models.TipoUsuario
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.utils.cifrarPassword
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UsuariosRestRepositoryTest {
    private val usuariosRepository = UsuariosRestRepository()

    private val usuario = Usuario(
        id = ObjectId(50.toString().padStart(24, '0')),
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

    @Test
    fun findAll() = runTest {
        val res = usuariosRepository.findAll().toList()

        assertAll(
            { assertNotNull(res) },
            { assertEquals(10, res.size) },
        )
    }

    @Test
    fun findById() = runTest {
        val res = usuariosRepository.findById(usuario.id)
        assert(res.nombre == usuario.nombre)

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
        val res = usuariosRepository.update(
            Usuario(
                id = ObjectId("1".padStart(24, '0')),
                uuid = UUID.randomUUID().toString(),
                nombre = "actualizado",
                apellido = "actualizado",
                email = "actualizado",
                password = cifrarPassword("actualizado"),
                rol = TipoUsuario.ENCORDADOR
            )
        )

        assertAll(
            { assertEquals(ObjectId("1".padStart(24, '0')), res.id) },
            { assertEquals("actualizado", res.nombre) },
            { assertEquals("actualizado", res.apellido) },
            { assertEquals("actualizado", res.email) },
        )
    }

    @Test
    fun updateNoExiste() = runTest {
        assertThrows<RuntimeException> {
            usuariosRepository.update(
                Usuario(
                    id = ObjectId("98".padStart(24, '0')),
                    uuid = UUID.randomUUID().toString(),
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
    fun delete() = runTest {
        usuariosRepository.save(usuario)
        usuariosRepository.delete(usuario)

        val res = usuariosRepository.findAll().toList()

        assert(res.isEmpty())
    }

    @Test
    fun deleteNoExiste() = runTest {
        usuariosRepository.save(usuario)
        usuariosRepository.delete(
            Usuario(
                id = ObjectId("99".padStart(24, '0')),
                uuid = UUID.randomUUID().toString(),
                nombre = "borrado",
                apellido = "borrado",
                email = "borrado",
                password = cifrarPassword("James"),
                rol = TipoUsuario.ENCORDADOR
            )
        )

        val res = usuariosRepository.findAll()

        assert(res.first() == usuario)
    }
}