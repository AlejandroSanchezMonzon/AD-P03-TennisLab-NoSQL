package repositories.productos

import kotlinx.coroutines.flow.toList
import models.Maquina
import models.Producto
import models.TipoMaquina
import models.TipoProducto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
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

    @Test
    suspend fun findAll() {
        val res = productosRepository.findAll()

        assert(res.toList().isEmpty())
    }

    @Test
    suspend fun findById() {
        productosRepository.save(producto)

        val res = productosRepository.findById(producto.id)

        assert(res == producto)
    }

    @Test
    suspend fun findByIdNoExiste() {
        val res = productosRepository.findById("-5")

        assert(res == null)
    }

    @Test
    suspend fun save() {
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

        productosRepository.delete(producto)
    }

    @Test
    suspend fun update() {
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

        productosRepository.delete(producto)
    }

    @Test
    suspend fun deleteNoExiste() {
        val res = productosRepository.delete(producto)

        assert(res==null)
    }

    @Test
    suspend fun delete() {
        productosRepository.save(producto)

        val res = productosRepository.delete(producto)

        assert(res==producto)
    }
}