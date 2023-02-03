package es.dam.adp03_springmongodb.controllers

import es.dam.adp03_springmongodb.models.*
import es.dam.adp03_springmongodb.repositories.maquinas.IMaquinasRepository
import es.dam.adp03_springmongodb.repositories.pedidos.IPedidosRepository
import es.dam.adp03_springmongodb.repositories.productos.IProductosRepository
import es.dam.adp03_springmongodb.repositories.tareas.ITareasRepository
import es.dam.adp03_springmongodb.repositories.turnos.ITurnosRepository
import es.dam.adp03_springmongodb.repositories.usuarios.IUsuariosRepository
import es.dam.adp03_springmongodb.utils.cifrarPassword
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
@SpringBootTest
class MongoControllerTest {
    @Mock
    private lateinit var maquinasRepository: IMaquinasRepository

    @Mock
    private lateinit var pedidosRepository: IPedidosRepository

    @Mock
    private lateinit var productosRepository: IProductosRepository

    @Mock
    private lateinit var tareasRepository: ITareasRepository

    @Mock
    private lateinit var turnosRepository: ITurnosRepository

    @Mock
    private lateinit var usuariosRepository: IUsuariosRepository
    
    private lateinit var controller: MongoController


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

    private val pedido = Pedido(
        id = ObjectId(4.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tareas = listOf(tarea),
        productos = listOf(producto),
        estado = TipoEstado.TERMINADO,
        usuario = usuario,
        fechaTope = LocalDate.of(2022, 11, 14),
        fechaEntrada = LocalDate.of(2022, 9, 5),
        fechaProgramada = LocalDate.of(2022, 10, 23),
        fechaEntrega = LocalDate.of(2022, 10, 23),
        precio = 3590.72f
    )

    @Test
    fun listarMaquinas() = runTest {
        coEvery { maquinasRepository.findAll() } returns flowOf(maquina)

        val res = controller.listarMaquinas()
        assert(res == flowOf(maquina))

        coVerify(exactly = 1) { maquinasRepository.findAll() }
    }

    @Test
    fun encontrarMaquina() = runTest {
        coEvery { maquinasRepository.findById(maquina.id) } returns maquina

        val res = controller.encontrarMaquina(maquina.id)
        assert(res == maquina)

        coVerify(exactly = 1) { maquinasRepository.findById(maquina.id) }
    }

    @Test
    fun guardarMaquina() = runTest {
        coEvery { maquinasRepository.save(maquina) } returns maquina

        controller.guardarMaquina(maquina)
        val res = controller.listarMaquinas()
        assert(res?.first() == maquina)

        coVerify(exactly = 1) { maquinasRepository.save(maquina) }
    }

    @Test
    fun actualizarMaquina() = runTest {
        coEvery { maquinasRepository.save(maquina) } returns maquina

        val res = maquina.descripcion
        maquina.descripcion = "descripcion"
        controller.actualizarMaquina(maquina)
        assert(res != maquina.descripcion)

        coVerify(exactly = 1) { maquinasRepository.save(maquina) }
    }

    @Test
    fun borrarMaquina() = runTest {
        coEvery { maquinasRepository.delete(maquina) }

        maquinasRepository.save(maquina)
        controller.deleteAll()
        val res = controller.listarMaquinas()

        assert(res?.first() == null)

        coVerify(exactly = 1) { maquinasRepository.delete(maquina) }
    }

    @Test
    fun listarProductos() = runTest {
        coEvery { productosRepository.findAll() } returns flowOf(producto)

        val res = controller.listarProductos()
        assert(res == flowOf(producto))

        coVerify(exactly = 1) { productosRepository.findAll() }
    }

    @Test
    fun encontrarProducto() = runTest {
        coEvery { productosRepository.findById(producto.id) } returns producto

        val res = controller.encontrarProducto(producto.id)
        assert(res == producto)

        coVerify(exactly = 1) { productosRepository.findById(producto.id) }
    }

    @Test
    fun guardarProducto() = runTest {
        coEvery { productosRepository.save(producto) } returns producto

        controller.guardarProducto(producto)
        val res = controller.listarProductos()
        assert(res?.first() == producto)

        coVerify(exactly = 1) { productosRepository.save(producto) }
    }

    @Test
    fun actualizarProducto() = runTest {
        coEvery { productosRepository.save(producto) } returns producto

        val res = producto.modelo
        producto.modelo = "descripcion"
        controller.actualizarProducto(producto)
        assert(res != producto.modelo)

        coVerify(exactly = 1) { productosRepository.save(producto) }
    }

    @Test
    fun borrarProducto() = runTest {
        coEvery { productosRepository.delete(producto) }

        productosRepository.save(producto)
        controller.deleteAll()
        val res = controller.listarProductos()

        assert(res?.first() == null)

        coVerify(exactly = 1) { productosRepository.delete(producto) }
    }

    @Test
    fun listarTurnos() = runTest {
        coEvery { turnosRepository.findAll() } returns flowOf(turno)

        val res = controller.listarTurnos()
        assert(res == flowOf(turno))

        coVerify(exactly = 1) { turnosRepository.findAll() }
    }

    @Test
    fun encontrarTurno() = runTest {
        coEvery { turnosRepository.findById(turno.id) } returns turno

        val res = controller.encontrarTurno(turno.id)
        assert(res == turno)

        coVerify(exactly = 1) { turnosRepository.findById(turno.id) }
    }

    @Test
    fun guardarTurno() = runTest {
        coEvery { turnosRepository.save(turno) } returns turno

        controller.guardarTurno(turno)
        val res = controller.listarTurnos()
        assert(res?.first() == turno)

        coVerify(exactly = 1) { turnosRepository.save(turno) }
    }

    @Test
    fun actualizarTurno() = runTest {
        coEvery { turnosRepository.save(turno) } returns turno

        val res = turno.comienzo
        turno.comienzo = LocalDateTime.now()
        controller.actualizarProducto(producto)
        assert(res != turno.comienzo)

        coVerify(exactly = 1) { turnosRepository.save(turno) }
    }

    @Test
    fun borrarTurno() = runTest {
        coEvery { turnosRepository.delete(turno) }

        turnosRepository.save(turno)
        controller.deleteAll()
        val res = controller.listarTurnos()

        assert(res?.first() == null)

        coVerify(exactly = 1) { turnosRepository.delete(turno) }
    }

    @Test
    fun listarTareas() = runTest {
        coEvery { tareasRepository.findAll() } returns flowOf(tarea)

        val res = controller.listarTareas()
        assert(res == flowOf(tarea))

        coVerify(exactly = 1) { tareasRepository.findAll() }
    }

    @Test
    fun encontrarTarea() = runTest {
        coEvery { tareasRepository.findById(tarea.id) } returns tarea

        val res = controller.encontrarTarea(tarea.id)
        assert(res == tarea)

        coVerify(exactly = 1) { tareasRepository.findById(tarea.id) }
    }

    @Test
    fun guardarTarea() = runTest {
        coEvery { tareasRepository.save(tarea) } returns tarea

        controller.guardarTarea(tarea)
        val res = controller.listarTareas()
        assert(res?.first() == tarea)

        coVerify(exactly = 1) { tareasRepository.save(tarea) }
    }

    @Test
    fun actualizarTarea() = runTest {
        coEvery { tareasRepository.save(tarea) } returns tarea

        val res = tarea.descripcion
        tarea.descripcion = "descripcion"
        controller.actualizarTarea(tarea)
        assert(res != tarea.descripcion)

        coVerify(exactly = 1) { tareasRepository.save(tarea) }
    }

    @Test
    fun borrarTarea() = runTest {
        coEvery { tareasRepository.delete(tarea) }

        tareasRepository.save(tarea)
        controller.deleteAll()
        val res = controller.listarTareas()

        assert(res?.first() == null)

        coVerify(exactly = 1) { tareasRepository.delete(tarea) }
    }

    @Test
    fun listarUsuarios() = runTest {
        coEvery { usuariosRepository.findAll() } returns flowOf(usuario)

        val res = controller.listarUsuarios()
        assert(res == flowOf(usuario))

        coVerify(exactly = 1) { usuariosRepository.findAll() }
    }

    @Test
    fun encontrarUsuario() = runTest {
        coEvery { usuariosRepository.findById(usuario.id) } returns usuario

        val res = controller.encontrarUsuario(usuario.id)
        assert(res == usuario)

        coVerify(exactly = 1) { usuariosRepository.findById(usuario.id) }
    }

    @Test
    fun guardarUsuario() = runTest {
        coEvery { usuariosRepository.save(usuario) } returns usuario

        controller.guardarUsuario(usuario)
        val res = controller.listarUsuarios()
        assert(res?.first() == usuario)

        coVerify(exactly = 1) { usuariosRepository.save(usuario) }
    }

    @Test
    fun actualizarUsuario() = runTest {
        coEvery { usuariosRepository.save(usuario) } returns usuario

        val res = usuario.nombre
        usuario.nombre = "Pepe"
        controller.actualizarUsuario(usuario)
        assert(res != usuario.nombre)

        coVerify(exactly = 1) { usuariosRepository.save(usuario) }
    }

    @Test
    fun borrarUsuario() = runTest {
        coEvery { usuariosRepository.delete(usuario) }

        usuariosRepository.save(usuario)
        controller.deleteAll()
        val res = controller.listarUsuarios()

        assert(res?.first() == null)

        coVerify(exactly = 1) { usuariosRepository.delete(usuario) }
    }

    @Test
    fun listarPedidos() = runTest {
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        val res = controller.listarPedidos()
        assert(res == flowOf(pedido))

        coVerify(exactly = 1) { pedidosRepository.findAll() }
    }

    @Test
    fun encontrarPedido() = runTest {
        coEvery { pedidosRepository.findById(pedido.id) } returns pedido

        val res = controller.encontrarPedido(pedido.id)
        assert(res == pedido)

        coVerify(exactly = 1) { pedidosRepository.findById(pedido.id) }
    }

    @Test
    fun guardarPedido() = runTest {
        coEvery { pedidosRepository.save(pedido) } returns pedido

        controller.guardarPedido(pedido)
        val res = controller.listarPedidos()
        assert(res?.first() == pedido)

        coVerify(exactly = 1) { pedidosRepository.save(pedido) }
    }

    @Test
    fun actualizarPedido() = runTest {
        coEvery { pedidosRepository.save(pedido) } returns pedido

        val res = pedido.fechaEntrega
        pedido.fechaEntrega = LocalDate.now()
        controller.actualizarPedido(pedido)
        assert(res != pedido.fechaEntrega)

        coVerify(exactly = 1) { pedidosRepository.save(pedido) }
    }

    @Test
    fun borrarPedido() = runTest {
        coEvery { pedidosRepository.delete(pedido) }

        pedidosRepository.save(pedido)
        controller.deleteAll()
        val res = controller.listarPedidos()

        assert(res?.first() == null)

        coVerify(exactly = 1) { pedidosRepository.delete(pedido) }
    }
}