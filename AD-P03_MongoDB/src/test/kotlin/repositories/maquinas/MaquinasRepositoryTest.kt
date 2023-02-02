package repositories.maquinas

import kotlinx.coroutines.flow.toList
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


    @Test
    suspend fun findAll() {
        val res = maquinasRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
    suspend fun findById() {
        maquinasRepository.save(maquina)

        val res = maquinasRepository.findById(maquina.id)

        assert(res == maquina)
    }

    @Test
    suspend fun findByIdNoExiste() {
        val res = maquinasRepository.findById("-5")

        assert(res == null)
    }

    @Test
    suspend fun save() {
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

        maquinasRepository.delete(maquina)
    }

    @Test
    suspend fun update() {
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

        maquinasRepository.delete(maquina)
    }

    @Test
    suspend fun delete() {
        maquinasRepository.save(maquina)

        val res = maquinasRepository.delete(maquina)

        assert(res==maquina)
    }

    @Test
    suspend fun deleteNoExiste() {
        val res = maquinasRepository.delete(maquina)

        assert(res==null)
    }
}