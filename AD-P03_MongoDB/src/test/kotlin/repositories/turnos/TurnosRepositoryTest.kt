package repositories.turnos

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import models.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import repositories.maquinas.MaquinasRepository
import repositories.usuarios.UsuariosRepository
import utils.cifrarPassword
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TurnosRepositoryTest {
    private val usuariosRepository = UsuariosRepository()
    private val maquinasRepository = MaquinasRepository()
    private val turnosRepository = TurnosRepository()

    init {
        MockKAnnotations.init(this)
    }

    private val usuario = Usuario(
        id = "1",
        uuid = UUID.randomUUID(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.ENCORDADOR
    )

    val maquina = Maquina(
        id = "3",
        uuid = UUID.randomUUID(),
        marca = "Indoostrial",
        modelo = "2600",
        fechaAdquisicion = LocalDate.of(2022, 7, 12),
        numeroSerie = 3,
        tipo = TipoMaquina.ENCORDAR,
        descripcion = "mideManiobrabilidad = true, " +
                "balance = 400.0, " +
                "rigidez = 80.0"
    )

    private val turno = Turno(
        id = "0",
        uuid = UUID.randomUUID(),
        comienzo = LocalDateTime.of(2022, 12, 7, 17, 48),
        final = LocalDateTime.of(2022, 12, 7, 18, 30),
        maquina = maquina,
        encordador = usuario
    )

    @BeforeAll
     fun setUp() = runTest{
        turnosRepository.deleteAll()
        usuariosRepository.save(usuario)
        maquinasRepository.save(maquina)
    }
    @AfterEach
     fun tearDown() = runTest {
        turnosRepository.deleteAll()

    }

    @AfterAll
     fun tearAllDown() = runTest{
        turnosRepository.deleteAll()
        usuariosRepository.delete(usuario)
        maquinasRepository.delete(maquina)
    }

    @Test
     fun findAll() = runTest{
        val res = turnosRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
     fun findById()= runTest {
        turnosRepository.save(turno)

        val res = turnosRepository.findById(turno.id)

        assert(res == turno)
    }

    @Test
     fun findByIdNoExiste() = runTest{
        val res = turnosRepository.findById("-5")

        assert(res == null)
    }

    @Test
     fun save() = runTest{
        val res = turnosRepository.save(turno)

        assertAll(
            { assertEquals(res.id, turno.id) },
            { assertEquals(res.uuid, turno.uuid) },
            { assertEquals(res.comienzo, turno.comienzo) },
            { assertEquals(res.final, turno.final) },
            { assertEquals(res.maquina, turno.maquina) },
            { assertEquals(res.encordador, turno.encordador) }
        )
    }

    @Test
     fun update() = runTest{
        turnosRepository.save(turno)
        val operacion = turnosRepository.update(
            Turno(
                id = "0",
                uuid = UUID.randomUUID(),
                comienzo = LocalDateTime.of(2000, 1, 1, 1, 1),
                final = LocalDateTime.of(2000, 1, 1, 1, 1),
                maquina = maquina,
                encordador = usuario
            )
        )

        val res = turnosRepository.findById(operacion.id)

        assertAll(
            { assertEquals(res?.id, operacion.id) },
            { assertEquals(res?.uuid, operacion.uuid) },
            { assertEquals(res?.comienzo, operacion.comienzo) },
            { assertEquals(res?.final, operacion.final) },

        )
    }

    @Test
     fun delete() = runTest{
        turnosRepository.save(turno)

        val res = turnosRepository.delete(turno)

        assert(res)
    }
    @Test
     fun deleteNoExiste() = runTest{
        val res = turnosRepository.delete(turno)

        assert(!res)
    }
}