package repositories.usuarios

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
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
        id = "11",
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

    @Test
     fun findAll() = runTest{
        val res = usuariosRepository.findAll().toList()

        assertAll(
            { assertNotNull(res) },
            { assertEquals(10, res.size) },
        )
    }

    @Test
     fun findById() = runTest {
          val res =  usuariosRepository.findById(usuario.id)
        assert(res?.nombre == usuario.nombre)

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
     fun update()  = runTest{
        usuariosRepository.save(usuario)
        val res = usuariosRepository.update(
            Usuario(
                id = "1",
                uuid = UUID.randomUUID(),
                nombre = "actualizado",
                apellido = "actualizado",
                email = "actualizado",
                password = cifrarPassword("actualizado"),
                rol = TipoUsuario.ENCORDADOR
            )
        )

        assertAll(
            { assertEquals("1", res.id) },
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
     fun delete()  = runTest{
        usuariosRepository.save(usuario)

        val res = usuariosRepository.delete(usuario)

        assert(res)
    }

    @Test
     fun deleteNoExiste() = runTest {
        val delete = usuariosRepository.delete( Usuario(
            id = "99",
            uuid = UUID.randomUUID(),
            nombre = "borrado",
            apellido = "borrado",
            email = "borrado",
            password = cifrarPassword("James"),
            rol = TipoUsuario.ENCORDADOR
        ))

        println(delete)
        assert(!delete)

    }
}