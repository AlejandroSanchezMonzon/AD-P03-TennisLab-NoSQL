package repositories.pedidos

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import models.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import repositories.productos.ProductosRepository
import repositories.usuarios.UsuariosRepository
import utils.cifrarPassword
import java.time.LocalDate
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PedidosRepositoryTest {
    private val pedidosRepository: PedidosRepository = PedidosRepository()
    private val usuariosRepository: UsuariosRepository = UsuariosRepository()
    private val productosRepository: ProductosRepository = ProductosRepository()

    private val producto = Producto(
    id = "0",
    uuid = UUID.randomUUID(),
    tipo = TipoProducto.COMPLEMENTO,
    marca = "ADIDAS",
    modelo = "33-A",
    precio = 15.5f,
    stock = 35,
    )

    val usuario = Usuario(
    id = "1",
    uuid = UUID.randomUUID(),
    nombre = "Jude",
    apellido = "James",
    email = "jude@james.com",
    password = cifrarPassword("James"),
    rol = TipoUsuario.TENISTA
    )

    private val pedido = Pedido(
    id = "0",
    uuid = UUID.randomUUID(),
    tareas = null,
    productos = listOf(producto) ,
    estado = TipoEstado.EN_PROCESO,
    usuario = usuario,
    fechaTope = LocalDate.of(2022, 12, 15),
    fechaEntrada = LocalDate.of(2022, 11, 10),
    fechaProgramada = LocalDate.of(2022, 12, 10),
    fechaEntrega = LocalDate.of(2022, 12, 10),
    precio = 120.0f
    )

    init {
        MockKAnnotations.init(this)
    }

    @BeforeAll
    fun setUp() = runTest {
        pedidosRepository.deleteAll()
        usuariosRepository.save(usuario)
        productosRepository.save(producto)

    }

    @AfterEach
    suspend fun tearDown() = runTest {
        pedidosRepository.deleteAll()

    }

    @AfterAll
    suspend fun tearAllDown() {
        usuariosRepository.delete(usuario)
        productosRepository.delete(producto)
    }

    @Test
     fun findAll() = runTest{
        val res = pedidosRepository.findAll()

        res.toList().forEach { println(it) }

        assert(res.toList().isEmpty())
    }

    @Test
     fun findById() = runTest{
        pedidosRepository.save(pedido)

        val res = pedidosRepository.findById(pedido.id)

        assert(res == pedido)
    }

    @Test
     fun findByIdNoExiste() = runTest{
        val res = pedidosRepository.findById("-5")

        assert(res == null)

    }

    @Test
     fun save() = runTest{
        val res = pedidosRepository.save(pedido)

        assertAll(
            { assertEquals(res.id, pedido.id) },
            { assertEquals(res.uuid, pedido.uuid) },
            { assertEquals(res.estado, pedido.estado) },
            { assertEquals(res.usuario, pedido.usuario) },
            { assertEquals(res.fechaTope, pedido.fechaTope) },
            { assertEquals(res.fechaEntrada, pedido.fechaEntrada) },
            { assertEquals(res.fechaEntrega, pedido.fechaEntrega) },
            { assertEquals(res.fechaTope, pedido.fechaTope) },
            { assertEquals(res.fechaProgramada, pedido.fechaProgramada) },
            { assertEquals(res.precio, pedido.precio) }
        )

    }

    @Test
     fun update() = runTest{
        pedidosRepository.save(pedido)
        val operacion = pedidosRepository.update(
            Pedido(
                id = "0",
                uuid = UUID.randomUUID(),
                tareas = null,
                productos = listOf(producto) ,
                estado = TipoEstado.EN_PROCESO,
                usuario = usuario,
                fechaTope = LocalDate.of(2000, 1, 1),
                fechaEntrada = LocalDate.of(2000, 1, 1),
                fechaProgramada = LocalDate.of(2000, 1, 1),
                fechaEntrega = LocalDate.of(2000, 1, 1),
                precio = 120.0f
            )
        )

        val res = pedidosRepository.findById(operacion.id)

        assertAll(
            { assertEquals(res?.id, operacion.id) },
            { assertEquals(res?.uuid, operacion.uuid) },
            { assertEquals(res?.estado, operacion.estado) },
            { assertEquals(res?.usuario, operacion.usuario) },
            { assertEquals(res?.fechaTope, operacion.fechaTope) },
            { assertEquals(res?.fechaEntrada, operacion.fechaEntrada) },
            { assertEquals(res?.fechaEntrega, operacion.fechaEntrega) },
            { assertEquals(res?.fechaTope, operacion.fechaTope) },
            { assertEquals(res?.fechaProgramada, operacion.fechaProgramada) },
            { assertEquals(res?.precio, operacion.precio) }

        )
    }

    @Test
     fun delete() = runTest{
        pedidosRepository.save(pedido)

        val res = pedidosRepository.delete(pedido)

        assert(res)
    }

    @Test
     fun deleteNoExiste() = runTest{
        val res = pedidosRepository.delete(pedido)

        assert(!res)
    }
}