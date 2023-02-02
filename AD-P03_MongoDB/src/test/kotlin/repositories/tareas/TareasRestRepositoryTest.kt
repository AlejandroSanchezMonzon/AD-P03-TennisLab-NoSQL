package repositories.tareas

import kotlinx.coroutines.flow.toList
import models.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import repositories.maquinas.MaquinasRepository
import repositories.turnos.TurnosRepository
import repositories.usuarios.UsuariosRepository
import utils.cifrarPassword
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TareasRestRepositoryTest {
    private val usuariosRepository = UsuariosRepository()
    private val maquinasRepository = MaquinasRepository()
    private val turnosRepository = TurnosRepository()
    private val tareasRepository = TareasRestRepository()

    private val usuario = Usuario(
        id = "1",
        uuid = UUID.randomUUID(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.ENCORDADOR
    )

    private val maquina = Maquina(
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

    private val tarea = Tarea(
        id = "0",
        uuid = UUID.randomUUID(),
        precio = 100.0f,
        tipo = TipoTarea.ENCORDADO,
        descripcion = "tensionHorizontal = 22.5, " +
                "cordajeHorizontal = Luxilon, " +
                "tensionVertical = 22.5, " +
                "cordajeVertical = Luxilon, " +
                "nudos = 4",
        turno = turno
    )

    @BeforeAll
    suspend fun setUp() {
        usuariosRepository.save(usuario)
        maquinasRepository.save(maquina)
        turnosRepository.save(turno)
    }

    @AfterAll
    suspend fun tearDown() {
        usuariosRepository.delete(usuario)
        maquinasRepository.delete(maquina)
        turnosRepository.delete(turno)
    }

    @Test
    suspend fun findAll() {
        val res = tareasRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
    suspend fun findById() {
        tareasRepository.save(tarea)

        val res = tareasRepository.findById(tarea.id)

        assert(res == tarea)
    }

    @Test
    suspend fun findByIdNoExiste() {
        assertThrows<RuntimeException> {
            tareasRepository.findById("-5")
        }
    }

    @Test
    suspend fun save() {
        val res = tareasRepository.save(tarea)

        assertAll(
            { assertEquals(res.id, tarea.id) },
            { assertEquals(res.uuid, tarea.uuid) },
            { assertEquals(res.precio, tarea.precio) },
            { assertEquals(res.tipo, tarea.tipo) },
            { assertEquals(res.descripcion, tarea.descripcion) },
            { assertEquals(res.turno, tarea.turno) }
        )

        tareasRepository.delete(tarea)
    }

    @Test
    suspend fun update() {
        tareasRepository.save(tarea)
        val operacion = tareasRepository.update(
            Tarea(
                id = "0",
                uuid = UUID.randomUUID(),
                precio = 100.0f,
                tipo = TipoTarea.ENCORDADO,
                descripcion = "actualizada",
                turno = turno
            )
        )
        val res = tareasRepository.findById(operacion.id)

        assertAll(
            { assertEquals(res?.id, operacion.descripcion) }
        )

        tareasRepository.delete(tarea)
    }

    @Test
    suspend fun deleteNoExiste() {
        assertThrows<RuntimeException> {
            tareasRepository.delete(tarea)
        }

    }

    @Test
    suspend fun delete() {
        tareasRepository.save(tarea)

        val res = tareasRepository.delete(tarea)

        assert(res == tarea)
    }
}