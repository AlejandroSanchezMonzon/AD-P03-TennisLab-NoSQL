package es.dam.adp03_springmongodb.repositories.tareas

import com.mongodb.assertions.Assertions.assertNotNull
import es.dam.adp03_springmongodb.models.*
import es.dam.adp03_springmongodb.repositories.maquinas.IMaquinasRepository
import es.dam.adp03_springmongodb.repositories.turnos.ITurnosRepository
import es.dam.adp03_springmongodb.repositories.usuarios.IUsuariosRepository
import es.dam.adp03_springmongodb.utils.cifrarPassword
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
internal class TareasRestRepositoryTest {
    @Autowired
    private lateinit var usuariosRepository: IUsuariosRepository

    @Autowired
    private lateinit var maquinasRepository: IMaquinasRepository

    @Autowired
    private lateinit var turnosRepository: ITurnosRepository

    @Autowired
    private lateinit var tareasRepository: ITareasRepository

    init {
        MockKAnnotations.init(this)
    }

    private val usuario = Usuario(
        id = ObjectId(50.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.TENISTA
    )

    private val maquina = Maquina(
        id = ObjectId(0.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        marca = "Vevor",
        modelo = "2021",
        fechaAdquisicion = LocalDate.of(2021, 11, 19),
        numeroSerie = 15,
        tipo = TipoMaquina.ENCORDAR,
        descripcion = "tipoEncordaje = AUTOMATICA, " +
                "tensionMaxima = 30.0, " +
                "tensionMinima = 20.0"
    )

    private val turno = Turno(
        id = ObjectId(5.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        comienzo = LocalDateTime.of(2022, 12, 7, 17, 48),
        final = LocalDateTime.of(2022, 12, 7, 18, 30),
        maquina = maquina,
        encordador = usuario
    )

    private val producto = Producto(
        id = ObjectId(51.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.COMPLEMENTO,
        marca = "ADIDAS",
        modelo = "33-A",
        precio = 15.5f,
        stock = 35,
    )

    private val tarea = Tarea(
        id = ObjectId(4.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
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
    fun setUp() = runTest {
        tareasRepository.deleteAll()
        usuariosRepository.save(usuario)
        maquinasRepository.save(maquina)
        turnosRepository.save(turno)
    }

    @AfterEach
    fun tearDown() = runTest {
        tareasRepository.deleteAll()
    }

    @AfterAll
    fun tearAllDown() = runTest {
        usuariosRepository.delete(usuario)
        maquinasRepository.delete(maquina)
        turnosRepository.delete(turno)
    }

    @Test
    fun findAll() = runTest {

        val res = tareasRepository.findAll().toList()

        assertAll(
            { assertNotNull(res) },
            { assertEquals(200, res.size) },
        )
    }

    @Test
    fun findById() = runTest {
        val save = tareasRepository.save(tarea)
        println("save$save")

        val res = tareasRepository.findById(save.id)
        println("res$res")
        assert(res == tarea)
    }

    @Test
    fun findByIdNoExiste() = runTest {
        assertThrows<RuntimeException> {
            tareasRepository.findById(ObjectId("-5".padStart(24, '0')))
        }
    }

    @Test
    fun save() = runTest {
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
    fun update() = runTest {
        tareasRepository.save(tarea)
        val res = tareasRepository.save(
            Tarea(
                id = ObjectId("1".padStart(24, '0')),
                uuid = UUID.randomUUID().toString(),
                precio = 100.0f,
                tipo = TipoTarea.ENCORDADO,
                descripcion = "actualizada",
                turno = turno
            )
        )

        assertAll(
            { assertEquals(res.descripcion, res.descripcion) }
        )
    }

    @Test
    fun updateNoExiste() = runTest {
        assertThrows<RuntimeException> {
            tareasRepository.save(
                Tarea(
                    id = ObjectId("345".padStart(24, '0')),
                    uuid = UUID.randomUUID().toString(),
                    precio = 100.0f,
                    tipo = TipoTarea.ENCORDADO,
                    descripcion = "actualizado",
                    turno = turno
                )
            )
        }
    }

    @Test
    fun deleteNoExiste() = runTest {
        tareasRepository.save(tarea)
        tareasRepository.delete(
            Tarea(
                id = ObjectId(10.toString().padStart(24, '0')),
                uuid = UUID.randomUUID().toString(),
                precio = 5.0f,
                tipo = TipoTarea.ENCORDADO,
                descripcion = "Test",
                turno = turno
            )
        )

        val res = tareasRepository.findAll()

        assert(res.first() == tarea)
    }

    @Test
    fun delete() = runTest {
        tareasRepository.save(tarea)
        tareasRepository.delete(tarea)

        val res = tareasRepository.findAll().toList()

        assert(res.isEmpty())
    }
}