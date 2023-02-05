package repositories.productos

import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import models.Producto
import models.TipoProducto
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProductosRepositoryTest {
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

    init {
        MockKAnnotations.init(this)
    }

    @BeforeAll
    fun setUp() = runTest {
        productosRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() = runTest{
        productosRepository.deleteAll()
    }

    @Test
     fun findAll() = runTest{
        val res = productosRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
     fun findById() = runTest{
        productosRepository.save(producto)

        val res = productosRepository.findById(producto.id)

        assert(res == producto)
    }

    @Test
     fun findByIdNoExiste()= runTest {
        val res = productosRepository.findById("-5")

        assert(res == null)
    }

    @Test
     fun save() = runTest{
        val res = productosRepository.save(producto)

        assertAll(
            { assertEquals(res.id, producto.id) },
            { assertEquals(res.uuid, producto.uuid) },
            { assertEquals(res.tipo, producto.tipo) },
            { assertEquals(res.marca, producto.marca) },
            { assertEquals(res.modelo, producto.modelo) },
            { assertEquals(res.precio, producto.precio) },
            { assertEquals(res.stock, producto.stock) },
        )
    }

    @Test
     fun update()= runTest {
        val operacion = productosRepository.update(
            Producto(
                id = "0",
                uuid = UUID.randomUUID(),
                tipo = TipoProducto.COMPLEMENTO,
                marca = "actualizado",
                modelo = "actualizado",
                precio = 15.5f,
                stock = 35,
            )
        )
        val res = productosRepository.findById(operacion.id)

        assertAll(
            { assertEquals(res?.id, operacion.id) },
            { assertEquals(res?.uuid, operacion.uuid) },
            { assertEquals(res?.tipo, operacion.tipo) },
            { assertEquals(res?.marca, operacion.marca) },
            { assertEquals(res?.modelo, operacion.modelo) },
            { assertEquals(res?.precio, producto.precio) },
            { assertEquals(res?.stock, operacion.stock) },

        )
    }

    @Test
     fun deleteNoExiste()= runTest {
        val res = productosRepository.delete(producto)

        assert(!res)
    }

    @Test
     fun delete() = runTest{
        productosRepository.save(producto)

        val res = productosRepository.delete(producto)

        assert(res)
    }
}