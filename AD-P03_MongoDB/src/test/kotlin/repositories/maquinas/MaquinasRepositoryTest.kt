package repositories.maquinas

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import models.Maquina
import models.TipoMaquina
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MaquinasRepositoryTest {
    private val maquinasRepository: MaquinasRepository = MaquinasRepository()

    private val maquina = Maquina(
        id = "3",
        uuid = UUID.randomUUID(),
        marca = "Indoostrial",
        modelo = "2600",
        fechaAdquisicion = LocalDate.of(2022, 7, 12),
        numeroSerie = 3,
        tipo = TipoMaquina.PERSONALIZAR,
        descripcion = "mideManiobrabilidad = true, " +
        "balance = 400.0, " +
        "rigidez = 80.0"
    )

    init {
        MockKAnnotations.init(this)
    }

    @BeforeAll
     fun setUp() = runTest {

        maquinasRepository.deleteAll()
    }

    @AfterEach
     fun tearDown() = runTest{
        maquinasRepository.deleteAll()
    }

    @Test
     fun findAll()= runTest {
        val res = maquinasRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
     fun findById() = runTest{
        maquinasRepository.save(maquina)

        val res = maquinasRepository.findById(maquina.id)

        assert(res == maquina)
    }

    @Test
     fun findByIdNoExiste() = runTest{
        val res = maquinasRepository.findById("-5")

        assert(res == null)
    }

    @Test
     fun save()= runTest {
        val res = maquinasRepository.save(maquina)

        assertAll(
            { assertEquals(res.id, maquina.id) },
            { assertEquals(res.uuid, maquina.uuid) },
            { assertEquals(res.marca, maquina.marca) },
            { assertEquals(res.modelo, maquina.modelo) },
            { assertEquals(res.fechaAdquisicion, maquina.fechaAdquisicion) },
            { assertEquals(res.numeroSerie, maquina.numeroSerie) },
            { assertEquals(res.tipo, maquina.tipo) },
            { assertEquals(res.descripcion, maquina.descripcion) }

        )
    }

    @Test
     fun update()= runTest {
        maquinasRepository.save(maquina)
        val operacion = maquinasRepository.update(
            Maquina(
            id = "3",
            uuid = UUID.randomUUID(),
            marca = "actualizada",
            modelo = "actualizada",
            fechaAdquisicion = LocalDate.of(2022, 7, 12),
            numeroSerie = 3,
            tipo = TipoMaquina.PERSONALIZAR,
            descripcion = "actualizada"
            )
        )
        val res = maquinasRepository.findById(operacion.id)

        assertAll(
            { assertEquals(res?.id, operacion.id) },
            { assertEquals(res?.uuid, operacion.uuid) },
            { assertEquals(res?.marca, operacion.marca) },
            { assertEquals(res?.modelo, operacion.modelo) },
            { assertEquals(res?.fechaAdquisicion, operacion.fechaAdquisicion) },
            { assertEquals(res?.numeroSerie, operacion.numeroSerie) },
            { assertEquals(res?.tipo, operacion.tipo) },
            { assertEquals(res?.descripcion, operacion.descripcion) }

        )
    }

    @Test
     fun delete() = runTest{
        maquinasRepository.save(maquina)
        val res = maquinasRepository.delete(maquina)

        assert(res)
    }

    @Test
     fun deleteNoExiste()= runTest {
        val res = maquinasRepository.delete(maquina)

        assert(!res)
    }
}