package repositories.tareas

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
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
internal class TareasRepositoryTest {
    private val usuariosRepository = UsuariosRepository()
    private val maquinasRepository = MaquinasRepository()
    private val turnosRepository = TurnosRepository()
    private val tareasRepository = TareasRepository()

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

    @BeforeEach
     fun setUp() = runTest{
        tareasRepository.deleteAll()
        usuariosRepository.save(usuario)
        maquinasRepository.save(maquina)
        turnosRepository.save(turno)
    }

    @AfterEach
    fun tearDown() = runTest{
        tareasRepository.deleteAll()
    }

    @AfterAll
     fun tearAllDown() = runTest{
        usuariosRepository.delete(usuario)
        maquinasRepository.delete(maquina)
        turnosRepository.delete(turno)
    }

    @Test
     fun findAll() = runTest{
        val res = tareasRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
     fun findById() = runTest{
        val prueba = tareasRepository.save(tarea)

        val res = tareasRepository.findById("0")
        println(prueba)
        println(res)
        println(tarea)
        tareasRepository.findAll().toList().forEach { println(it) }
        assert(res == tarea)
    }

    @Test
     fun findByIdNoExiste() = runTest{
        val res = tareasRepository.findById("-5")

        assert(res == null)
    }

    @Test
     fun save() = runTest{
        val res = tareasRepository.save(tarea)

        assertAll(
            { assertEquals(res.id, tarea.id) },
            { assertEquals(res.uuid, tarea.uuid) },
            { assertEquals(res.precio, tarea.precio) },
            { assertEquals(res.tipo, tarea.tipo) },
            { assertEquals(res.descripcion, tarea.descripcion) },
            { assertEquals(res.turno, tarea.turno) }
        )
    }

    @Test
     fun update() = runTest{
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
            { assertEquals(res?.descripcion, operacion.descripcion) }
        )
    }

    @Test
     fun deleteNoExiste() = runTest{
        val res = tareasRepository.delete(tarea)

        assert(!res)
    }

    @Test
     fun delete() = runTest{
        tareasRepository.save(tarea)

        val res = tareasRepository.delete(tarea)

        assert(res)
    }


}