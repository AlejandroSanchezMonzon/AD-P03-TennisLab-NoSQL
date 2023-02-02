package repositories.turnos

import kotlinx.coroutines.flow.toList
import models.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
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
    suspend fun setUp() {
        usuariosRepository.save(usuario)
        maquinasRepository.save(maquina)
    }

    @AfterAll
    suspend fun tearDown() {
        usuariosRepository.delete(usuario)
        maquinasRepository.delete(maquina)
    }

    @Test
    suspend fun findAll() {
        val res = turnosRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
    suspend fun findById() {
        turnosRepository.save(turno)

        val res = turnosRepository.findById(turno.id)

        assert(res == turno)
    }

    @Test
    suspend fun findByIdNoExiste() {
        val res = turnosRepository.findById("-5")

        assert(res == null)

    }

    @Test
    suspend fun save() {
        val res = turnosRepository.save(turno)

        assertAll(
            { assertEquals(res.id, turno.id) },
            { assertEquals(res.uuid, turno.uuid) },
            { assertEquals(res.comienzo, turno.comienzo) },
            { assertEquals(res.final, turno.final) },
            { assertEquals(res.maquina, turno.maquina) },
            { assertEquals(res.encordador, turno.encordador) }
        )

        turnosRepository.delete(turno)
    }

    @Test
    suspend fun update() {
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

        turnosRepository.delete(turno)
    }

    @Test
    suspend fun delete() {
        turnosRepository.save(turno)

        val res = turnosRepository.delete(turno)

        assert(res==turno)
    }
    @Test
    suspend fun deleteNoExiste() {
        val res = turnosRepository.delete(turno)

        assert(res == null)
    }
}