package controllers

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import models.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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
import utils.cifrarPassword
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

    val maquina = Maquina(
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

    val producto = Producto(
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

    val pedido = Pedido(
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

    val turno = Turno(
        id = "0",
        uuid = UUID.randomUUID(),
        comienzo = LocalDateTime.of(2022, 12, 7, 17, 48),
        final = LocalDateTime.of(2022, 12, 7, 18, 30),
        maquina = maquina,
        encordador = usuario
    )

    val tarea = Tarea(
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

    @Test
    fun descargarDatos() {
    }

    @Test
    fun guardarMaquina() {
    }

    @Test
    fun borrarMaquina() {
    }

    @Test
    fun actualizarMaquina() {
    }

    @Test
    fun encontrarMaquina() {

    }

    @Test
    fun listarMaquinas() {

    }

    @Test
    fun encontrarProducto() {
    }

    @Test
    fun listarProductos() {
    }

    @Test
    fun guardarProducto() {
    }

    @Test
    fun borrarProducto() {
    }

    @Test
    fun actualizarProducto() {
    }

    @Test
    fun encontrarTurno() {
    }

    @Test
    fun listarTurnos() {
    }

    @Test
    fun isTurnoOk() {
    }

    @Test
    fun guardarTurno() {
    }

    @Test
    fun borrarTurno() {
    }

    @Test
    fun actualizarTurno() {
    }

    @Test
    fun encontrarPedido() {
    }

    @Test
    fun listarPedidos() {
    }

    @Test
    fun guardarPedido() {
    }

    @Test
    fun borrarPedido() {
    }

    @Test
    fun actualizarPedido() {
    }

    @Test
    fun guardarUsuario() {
    }

    @Test
    fun borrarUsuario() {
    }

    @Test
    fun actualizarUsuario() {
    }

    @Test
    fun encontrarUsuario() {
    }

    @Test
    fun listarUsuarios() {
    }

    @Test
    fun guardarTarea() {
    }

    @Test
    fun borrarTarea() {
    }

    @Test
    fun actualizarTarea() {
    }

    @Test
    fun listarTarea() {
    }

    @Test
    fun encontrarTarea() {
    }
}