package controllers

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import models.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import repositories.maquinas.MaquinasRepository
import repositories.pedidos.PedidosRepository
import repositories.productos.ProductosRepository
import repositories.tareas.TareasRepository
import repositories.tareas.TareasRestRepository
import repositories.turnos.TurnosRepository
import repositories.usuarios.UsuariosCacheRepository
import repositories.usuarios.UsuariosRepository
import repositories.usuarios.UsuariosRestRepository
import services.sqldelight.SqlDeLightClient
import usuarioSesion
import utils.cifrarPassword
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
internal class MongoControllerTest {
    @MockK
    var maquinasRepository = MaquinasRepository()

    @MockK
    var pedidosRepository = PedidosRepository()

    @MockK
    var productosRepository = ProductosRepository()

    @MockK
    var tareasRepository = TareasRepository()

    @MockK
    var tareasRestRepository = TareasRestRepository()

    @MockK
    var turnosRepository = TurnosRepository()

    @MockK
    var usuariosRepository = UsuariosRepository()

    @MockK
    var usuariosRestRepository = UsuariosRestRepository()

    @MockK
    var usuariosCacheRepository = UsuariosCacheRepository(SqlDeLightClient())

    @InjectMockKs
    lateinit var controller: MongoController

    private var usuarioDeSesion = Usuario(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788975"),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.ADMIN_ENCARGADO
    )

    private val maquina = Maquina(
        id = "3",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788976"),
        marca = "Indoostrial",
        modelo = "2600",
        fechaAdquisicion = LocalDate.of(2022, 7, 12),
        numeroSerie = 3,
        tipo = TipoMaquina.PERSONALIZAR,
        descripcion = "mideManiobrabilidad = true, " +
                "balance = 400.0, " +
                "rigidez = 80.0"
    )

    private val maquinaActualizada = Maquina(
        id = "3",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788976"),
        marca = "actualizada",
        modelo = "actualizada",
        fechaAdquisicion = LocalDate.of(2022, 7, 12),
        numeroSerie = 3,
        tipo = TipoMaquina.PERSONALIZAR,
        descripcion = "actualizada")

    private val producto = Producto(
        id = "0",
        uuid =UUID.fromString("137bff15-7551-49e4-82f6-958189788977"),
        tipo = TipoProducto.COMPLEMENTO,
        marca = "ADIDAS",
        modelo = "33-A",
        precio = 15.5f,
        stock = 35,
    )

    private val productoActualizado = Producto(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788977"),
        tipo = TipoProducto.COMPLEMENTO,
        marca = "actualizada",
        modelo = "actualizada",
        precio = 15.5f,
        stock = 35,
    )

    private val usuario = Usuario(
        id = "1",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788978"),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.TENISTA
    )

    private val usuarioActualizado = Usuario(
        id = "1",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788978"),
        nombre = "actualizada",
        apellido = "actualizada",
        email = "actualizada",
        password = cifrarPassword("James"),
        rol = TipoUsuario.TENISTA
    )

    private val pedido = Pedido(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788979"),
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

    private val pedidoActualizado =  Pedido(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788979"),
        tareas = null,
        productos = listOf(producto) ,
        estado = TipoEstado.EN_PROCESO,
        usuario = usuario,
        fechaTope = LocalDate.of(2000, 1, 1),
        fechaEntrada = LocalDate.of(2000, 1, 1),
        fechaProgramada = LocalDate.of(2000, 1, 1),
        fechaEntrega = LocalDate.of(2000, 1, 1),
        precio = 120.0f)

    private val turno = Turno(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788980"),
        comienzo = LocalDateTime.of(2022, 12, 7, 17, 48),
        final = LocalDateTime.of(2022, 12, 7, 18, 30),
        maquina = maquina,
        encordador = usuario
    )

    private val turnoActualizado = Turno(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788980"),
        comienzo = LocalDateTime.of(2000, 1, 1, 1, 1),
        final = LocalDateTime.of(2000, 1, 1, 1, 1),
        maquina = maquina,
        encordador = usuario)

    private val tarea = Tarea(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788974"),
        precio = 100.0f,
        tipo = TipoTarea.ENCORDADO,
        descripcion = "tensionHorizontal = 22.5, " +
                "cordajeHorizontal = Luxilon, " +
                "tensionVertical = 22.5, " +
                "cordajeVertical = Luxilon, " +
                "nudos = 4",
        turno = turno
    )

    private val tareaActualizada = Tarea(
        id = "0",
        uuid = UUID.fromString("137bff15-7551-49e4-82f6-958189788974"),
        precio = 100.0f,
        tipo = TipoTarea.ENCORDADO,
        descripcion = "actualizada",
        turno = turno)

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun descargarDatos() = runTest {
    }

    @Test
    fun guardarMaquina() = runTest {
        coEvery { maquinasRepository.save(maquina) } returns maquina
        coEvery { maquinasRepository.findAll() } returns flowOf(maquina)

        usuarioSesion = usuarioDeSesion
        controller.guardarMaquina(maquina)

        val res = controller.listarMaquinas()
        assert(res?.first() == maquina)
    }

    @Test
    fun borrarMaquina() = runTest {
        coEvery { maquinasRepository.delete(maquina) }
        coEvery { maquinasRepository.save(maquina) } returns maquina
        coEvery { maquinasRepository.findAll() } returns flowOf(maquina)
        coEvery { turnosRepository.findAll() } returns flowOf(turno)

        usuarioSesion = usuarioDeSesion
        controller.guardarMaquina(maquina)
        val res = assertThrows<IllegalArgumentException> {
            controller.borrarMaquina(maquina)
        }

        assertEquals("Antes de realizar la operación, elimine o actualice el turno/s asociados a esta máquina. ", res.message)
    }

    //TODO: no funciona
    @Test
    fun actualizarMaquina() = runTest {
        coEvery { maquinasRepository.save(maquina) } returns maquina
        coEvery { maquinasRepository.update(maquinaActualizada) } returns maquinaActualizada
        coEvery { maquinasRepository.findAll() } returns flowOf(maquina)

        usuarioSesion = usuarioDeSesion
        controller.guardarMaquina(maquina)
        val res2 = controller.listarMaquinas()?.toList()
        res2?.forEach {println(it)}
        println("hola")
        controller.actualizarMaquina(
            maquinaActualizada
        )
        val res = controller.listarMaquinas()?.toList()
        res?.forEach {println(it)}

        assertAll(
            { assertEquals(res?.get(0)?.id, maquina.id) },
            { assertEquals(res?.get(0)?.marca, "actualizada") },
            { assertEquals(res?.get(0)?.modelo, "actualizada") },
            { assertEquals(res?.get(0)?.descripcion, "actualizada") }
        )
    }

    @Test
    fun encontrarMaquina() = runTest {
        coEvery { maquinasRepository.save(maquina) } returns maquina
        coEvery { maquinasRepository.findById(maquina.id) } returns maquina

        usuarioSesion = usuarioDeSesion
        controller.guardarMaquina(maquina)
        val res = controller.encontrarMaquina(maquina.id)
        println(res)
        println(maquina)
        assert(res == maquina)
    }

    @Test
    fun listarMaquinas() = runTest {
        coEvery { maquinasRepository.findAll() } returns flowOf(maquina)

        val res = controller.listarMaquinas()?.toList()
        assert(res?.get(0) ==maquina)
    }

    @Test
    fun encontrarProducto() = runTest {
        coEvery { productosRepository.save(producto) } returns producto
        coEvery { productosRepository.findById(producto.id) } returns producto

        usuarioSesion = usuarioDeSesion
        controller.guardarProducto(producto)
        val res = controller.encontrarProducto(producto.id)
        assert(res == producto)
    }

    //TODO: problema del stUp hay un dato gusradado que no debería haber
    @Test
    fun listarProductos() = runTest {
        coEvery { productosRepository.findAll() } returns flowOf(producto)
        coEvery { productosRepository.deleteAll() } returns Unit


        usuarioSesion = usuarioDeSesion
        val res = controller.listarProductos()?.toList()
        res?.forEach {println(it)}
        assert(res?.get(0)==producto)
    }

    @Test
    fun guardarProducto() = runTest {
        coEvery { productosRepository.save(producto) } returns producto
        coEvery { productosRepository.findAll() } returns flowOf(producto)

        usuarioSesion = usuarioDeSesion
        controller.guardarProducto(producto)

        val res = controller.listarProductos()
        assert(res?.first() == producto)
    }

    @Test
    fun borrarProducto() = runTest {
        coEvery { productosRepository.delete(producto) }
        coEvery { productosRepository.save(producto) } returns producto
        coEvery { productosRepository.findAll() } returns flowOf(producto)
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        controller.guardarProducto(producto)
        val res = assertThrows<IllegalArgumentException> {
            controller.borrarProducto(producto)
        }

        assertEquals("Antes de realizar la operación, elimine o actualice el pedido/s asociados a este producto.", res.message)
    }

    //TODO: no funciona
    @Test
    fun actualizarProducto() = runTest {
        coEvery { productosRepository.save(producto) } returns producto
        coEvery { productosRepository.update(productoActualizado) } returns productoActualizado
        coEvery { productosRepository.findAll() } returns flowOf(producto)


        usuarioSesion = usuarioDeSesion
        controller.guardarProducto(producto)
        println("hola")
        controller.listarProductos()?.toList()?.forEach {"guardar"+println(it)}
        controller.actualizarProducto(
            productoActualizado
        )
        controller.listarProductos()?.toList()?.forEach {"actualizar"+println(it)}
        val res = controller.listarProductos()?.toList()
        res?.forEach {"findAll"+println(it)}

        assertAll(
            { assertEquals(res?.get(0)?.id, maquina.id) },
            { assertEquals(res?.get(0)?.marca, "actualizada") },
            { assertEquals(res?.get(0)?.modelo, "actualizada") },
        )
    }

    @Test
    fun encontrarTurno() = runTest {
        coEvery { turnosRepository.save(turno) } returns turno
        coEvery { turnosRepository.findById(turno.id) } returns turno
        coEvery { turnosRepository.findAll() } returns flowOf(turno)

        usuarioSesion = usuarioDeSesion
        controller.guardarTurno(turno)
        val res = controller.encontrarTurno(turno.id)
        assert(res == turno)
    }

    //TODO: problema del stup, hay un turno almacenado que no debería haber
    @Test
    fun listarTurnos() = runTest {
        coEvery { turnosRepository.findAll() } returns flowOf(turno)

        usuarioSesion = usuarioDeSesion
        val res = controller.listarTurnos()?.toList()
        res?.forEach {println(it)}
        assert(res?.get(0) ==turno)
    }

    @Test
    fun isTurnoOk() = runTest {
    }

    @Test
    fun guardarTurno() = runTest {
        coEvery { turnosRepository.save(turno) } returns turno
        coEvery { turnosRepository.findAll() } returns flowOf(turno)

        usuarioSesion = usuarioDeSesion
        controller.guardarTurno(turno)

        val res = controller.listarTurnos()
        assert(res?.first() == turno)
    }

    @Test
    fun borrarTurno() = runTest {
        coEvery { turnosRepository.delete(turno) }
        coEvery { turnosRepository.save(turno) } returns turno
        coEvery { turnosRepository.findAll() } returns flowOf(turno)
        coEvery { tareasRepository.findAll() } returns flowOf(tarea)

        usuarioSesion = usuarioDeSesion
        controller.guardarTurno(turno)
        val res = assertThrows<IllegalArgumentException> {
            controller.borrarTurno(turno)
        }

        assertEquals("Antes de realizar la operación, elimine o actualice la tarea/s asociados a este turno. ", res.message)
    }

    //TODO: nu funciona
    @Test
    fun actualizarTurno() = runTest {
        coEvery { turnosRepository.save(turno) } returns turno
        coEvery { turnosRepository.update(turnoActualizado) } returns turnoActualizado
        coEvery { turnosRepository.findAll() } returns flowOf(turno)
        coEvery { turnosRepository.findAll() } returns flowOf(turno)


        usuarioSesion = usuarioDeSesion
        controller.guardarTurno(turno)
        println("hola")
        controller.listarTurnos()?.toList()?.forEach {"guardar"+println(it)}
        controller.actualizarTurno(
            turnoActualizado
        )
        controller.listarTurnos()?.toList()?.forEach {"actualizar"+println(it)}
        val res = controller.listarTurnos()?.toList()
        res?.forEach {"findAll"+println(it)}

        assertAll(
            { assertEquals(res?.get(0)?.id, turno.id) },
            { assertEquals(res?.get(0)?.comienzo, LocalDateTime.of(2000, 1, 1, 1, 1)) },
            { assertEquals(res?.get(0)?.final, LocalDateTime.of(2000, 1, 1, 1, 1)) },
        )
    }

    @Test
    fun encontrarPedido() = runTest {
        coEvery { pedidosRepository.save(pedido) } returns pedido
        coEvery { pedidosRepository.findById(pedido.id) } returns pedido
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        controller.guardarPedido(pedido)
        controller.listarPedidos()?.toList()?.forEach { println(it)}
        val res = controller.encontrarPedido(pedido.id)
        assert(res == pedido)
    }

    //TODO:setup
    @Test
    fun listarPedidos() = runTest {
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        val res = controller.listarPedidos()?.toList()
        assert(res?.get(0) == pedido)
    }

    @Test
    fun guardarPedido() = runTest {
        coEvery { pedidosRepository.save(pedido) } returns pedido
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        controller.guardarPedido(pedido)

        val res = controller.listarPedidos()
        assert(res?.first() == pedido)
    }

    //TODO:wtf no borrra
    @Test
    fun borrarPedido() = runTest {
        coEvery { pedidosRepository.delete(pedido)} returns true
        coEvery { pedidosRepository.save(pedido) } returns pedido
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        controller.guardarPedido(pedido)
        controller.listarPedidos()?.toList()?.forEach { println(it) }
        controller.borrarPedido(pedido)
        val res = controller.listarPedidos()?.toList()
        res?.forEach { println(it) }

        res?.isEmpty()?.let { assert(it) }
    }

    //TODO: no funciona
    @Test
    fun actualizarPedido() = runTest {
        coEvery { pedidosRepository.save(pedido) } returns pedido
        coEvery { pedidosRepository.update(pedidoActualizado) } returns pedidoActualizado
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)
        coEvery { turnosRepository.findAll() } returns flowOf(turno)

        usuarioSesion = usuarioDeSesion
        controller.guardarPedido(pedido)
        println("hola")
        controller.listarPedidos()?.toList()?.forEach {println("guardar $it")}
        controller.actualizarPedido(
            pedidoActualizado
        )
        controller.listarPedidos()?.toList()?.forEach {println("actualizar $it")}
        val res = controller.listarPedidos()?.toList()
        res?.forEach {println("findAll $it")}

        assertAll(
            { assertEquals(res?.get(0)?.id, maquina.id) },
            { assertEquals(res?.get(0)?.fechaEntrada, LocalDate.of(2000, 1, 1)) },
            { assertEquals(res?.get(0)?.fechaProgramada, LocalDate.of(2000, 1, 1)) },
            { assertEquals(res?.get(0)?.fechaTope, LocalDate.of(2000, 1, 1)) }
        )
    }

    @Test
    fun guardarUsuario() = runTest {
        coEvery { usuariosRepository.save(usuario) } returns usuario
        coEvery { usuariosCacheRepository.save(usuario) } returns usuario
        coEvery { usuariosRestRepository.save(usuario) } returns usuario
        coEvery { usuariosRepository.findAll() } returns flowOf(usuario)

        usuarioSesion = usuarioDeSesion
        controller.guardarUsuario(usuario)

        val res = controller.listarUsuarios()
        assert(res?.first() == usuario)
    }

    @Test
    fun borrarUsuario() = runTest {
        coEvery { usuariosRepository.delete(usuario) }
        coEvery { usuariosCacheRepository.delete(usuario) }
        coEvery { usuariosRestRepository.delete(usuario) }
        coEvery { usuariosRepository.save(usuario) } returns usuario
        coEvery { usuariosRestRepository.save(usuario) } returns usuario
        coEvery { usuariosCacheRepository.save(usuario) } returns usuario
        coEvery { usuariosRepository.findAll() } returns flowOf(usuario)
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        controller.guardarUsuario(usuario)

        val res = assertThrows<IllegalArgumentException> {
            controller.borrarUsuario(usuario)
        }

        assertEquals("Antes de realizar la operación, elimine o actualice el pedido/s asociados a este usuario. ", res.message)
    }

    //TODO: no funciona
    @Test
    fun actualizarUsuario() = runTest {
        coEvery { usuariosRepository.save(usuario) } returns usuario
        coEvery { usuariosRestRepository.save(usuario) } returns usuario
        coEvery { usuariosCacheRepository.save(usuario) } returns usuario
        coEvery { usuariosRepository.update(usuarioActualizado) } returns usuarioActualizado
        coEvery { usuariosCacheRepository.update(usuarioActualizado) } returns usuarioActualizado
        coEvery { usuariosRestRepository.update(usuarioActualizado) } returns usuarioActualizado
        coEvery { usuariosRepository.findAll() } returns flowOf(usuario)
        coEvery { turnosRepository.findAll() } returns flowOf(turno)


        usuarioSesion = usuarioDeSesion
        controller.guardarUsuario(usuario)
        println("hola")
        controller.listarUsuarios()?.toList()?.forEach {"guardar"+println(it)}
        controller.actualizarUsuario(
            usuarioActualizado
        )
        controller.listarUsuarios()?.toList()?.forEach {"actualizar"+println(it)}
        val res = controller.listarUsuarios()?.toList()
        res?.forEach {"findAll"+println(it)}

        assertAll(
            { assertEquals(res?.get(0)?.id, usuario.id) },
            { assertEquals(res?.get(0)?.nombre, "actualizada") },
            { assertEquals(res?.get(0)?.apellido, "actualizada") },
            { assertEquals(res?.get(0)?.email, "actualizada") }
        )
    }

    @Test
    fun encontrarUsuario() = runTest {
        coEvery { usuariosRepository.save(usuario) } returns usuario
        coEvery { usuariosRestRepository.save(usuario) } returns usuario
        coEvery { usuariosCacheRepository.save(usuario) } returns usuario
        coEvery { usuariosRepository.findById(usuario.id) } returns usuario
        coEvery { usuariosRepository.findAll() } returns flowOf(usuario)

        usuarioSesion = usuarioDeSesion
        controller.guardarUsuario(usuario)
        val res = controller.encontrarUsuario(usuario.id)
        assert(res == usuario)
    }

    //TODO: setup
    @Test
    fun listarUsuarios() = runTest {
        coEvery { usuariosRepository.findAll() } returns flowOf(usuario)
        coEvery { turnosRepository.findAll() } returns flowOf(turno)


        usuarioSesion = usuarioDeSesion
        val res = controller.listarUsuarios()?.toList()
        res?.forEach{println(it)}
        assert(res?.get(0) == usuario)
    }

    @Test
    fun guardarTarea() = runTest {
        coEvery { tareasRepository.save(tarea) } returns tarea
        coEvery { tareasRestRepository.save(tarea) } returns tarea
        coEvery { tareasRepository.findAll() } returns flowOf(tarea)

        usuarioSesion = usuarioDeSesion
        controller.guardarTarea(tarea)

        val res = controller.listarTarea()
        assert(res?.first() == tarea)
    }

    @Test
    fun borrarTarea() = runTest {
        coEvery { tareasRepository.delete(tarea) }
        coEvery { tareasRepository.save(tarea) } returns tarea
        coEvery { tareasRestRepository.save(tarea) } returns tarea
        coEvery { tareasRepository.findAll() } returns flowOf(tarea)
        coEvery { pedidosRepository.findAll() } returns flowOf(pedido)

        usuarioSesion = usuarioDeSesion
        controller.guardarTarea(tarea)
        val res = assertThrows<IllegalArgumentException> {
            controller.borrarTarea(tarea)
        }

        assertEquals("Antes de realizar la operación, elimine o actualice el pedido/s asociados a esta tarea.", res.message)
    }

    @Test
    fun actualizarTarea() = runTest {
        coEvery { tareasRepository.save(tarea) } returns tarea
        coEvery { tareasRestRepository.save(tarea) } returns tarea
        coEvery { tareasRepository.update(tareaActualizada) } returns tareaActualizada
        coEvery { tareasRestRepository.update(tareaActualizada) } returns tareaActualizada
        coEvery { tareasRepository.findAll() } returns flowOf(tarea)
        coEvery { turnosRepository.findAll() } returns flowOf(turno)


        usuarioSesion = usuarioDeSesion
        controller.guardarTarea(tarea)
        println("hola")
        controller.listarTarea()?.toList()?.forEach {"guardar"+println(it)}
        controller.actualizarTarea(
            tareaActualizada
        )
        controller.listarTarea()?.toList()?.forEach {"actualizar"+println(it)}
        val res = controller.listarTarea()?.toList()
        res?.forEach {"findAll"+println(it)}

        assertAll(
            { assertEquals(res?.get(0)?.id, tarea.id) },
            { assertEquals(res?.get(0)?.descripcion, "actualizada") }
        )
    }

    //TODO: setup
    @Test
    fun listarTareas() = runTest {
        coEvery { tareasRepository.findAll() } returns flowOf(tarea)

        usuarioSesion = usuarioDeSesion
        val res = controller.listarTarea()?.toList()
        assert(res?.get(0) == tarea)
    }

    @Test
    fun encontrarTarea() = runTest {
        coEvery { tareasRepository.save(tarea) } returns tarea
        coEvery { tareasRestRepository.save(tarea) } returns tarea
        coEvery { tareasRepository.findById(tarea.id) } returns tarea

        usuarioSesion = usuarioDeSesion
        controller.guardarTarea(tarea)
        val res = controller.encontrarTarea(tarea.id)
        assert(res == tarea)
    }
}