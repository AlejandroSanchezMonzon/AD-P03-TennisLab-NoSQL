/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.db

import es.dam.adp03_springmongodb.models.*
import es.dam.adp03_springmongodb.utils.cifrarPassword
import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Función que contiene una lista de Usurios con datos ficticios de prueba.
 *
 * @return La lista de Usuarios.
 */
fun getUsuariosInit() = listOf(
    Usuario(
        id = ObjectId(50.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Jude",
        apellido = "James",
        email = "jude@james.com",
        password = cifrarPassword("James"),
        rol = TipoUsuario.TENISTA
    ),
    Usuario(
        id = ObjectId(51.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Gracy",
        apellido = "Jonhs",
        email = "gracy@jonhs.com",
        password = cifrarPassword("Johns"),
        rol = TipoUsuario.TENISTA
    ),
    Usuario(
        id = ObjectId(52.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Bleck",
        apellido = "Mon",
        email = "bleck@mon.com",
        password = cifrarPassword("Men"),
        rol = TipoUsuario.ENCORDADOR
    ),
    Usuario(
        id = ObjectId(53.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Udel",
        apellido = "Geg",
        email = "udel@geg.com",
        password = cifrarPassword("Geg"),
        rol = TipoUsuario.ENCORDADOR
    ),
    Usuario(
        id = ObjectId(54.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Bred",
        apellido = "Widow",
        email = "bred@widow.com",
        password = cifrarPassword("Widow"),
        rol = TipoUsuario.ENCORDADOR
    ),
    Usuario(
        id = ObjectId(55.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Peter",
        apellido = "Beet",
        email = "peter@beet.com",
        password = cifrarPassword("Beet"),
        rol = TipoUsuario.ADMIN_ENCARGADO
    ),
    Usuario(
        id = ObjectId(56.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        nombre = "Lord",
        apellido = "Sir",
        email = "lord@sir.com",
        password = cifrarPassword("Sir"),
        rol = TipoUsuario.ADMIN_ENCARGADO
    )
)

/**
 * Función que contiene una lista de Turnos con datos ficticios de prueba.
 *
 * @return La lista de Turnos.
 */
suspend fun getTurnosInit() = listOf(
    Turno(
        id = ObjectId(5.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        comienzo = LocalDateTime.of(2022, 12, 7, 17, 48),
        final = LocalDateTime.of(2022, 12, 7, 18, 30),
        maquina = getMaquinasInit()[0],
        encordador = getUsuariosInit()[5]
    ),
    Turno(
        id = ObjectId(1.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        comienzo = LocalDateTime.of(2022, 12, 5, 16, 5),
        final = LocalDateTime.of(2022, 12, 6, 9, 0),
        maquina = getMaquinasInit()[1],
        encordador = getUsuariosInit()[3]
    ),
    Turno(
        id = ObjectId(2.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        comienzo = LocalDateTime.of(2022, 12, 1, 9, 0),
        final = LocalDateTime.of(2022, 12, 2, 20, 0),
        maquina = getMaquinasInit()[2],
        encordador = getUsuariosInit()[4]
    ),
    Turno(
        id = ObjectId(3.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        comienzo = LocalDateTime.of(2022, 8, 5, 7, 0),
        final = LocalDateTime.of(2022, 12, 4, 20, 0),
        maquina = getMaquinasInit()[3],
        encordador = getUsuariosInit()[6]
    )
)

/**
 * Función que contiene una lista de Productos con datos ficticios de prueba.
 *
 * @return La lista de Productos.
 */
fun getProductosInit() = listOf(
    Producto(
        id = ObjectId(51.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.COMPLEMENTO,
        marca = "ADIDAS",
        modelo = "33-A",
        precio = 15.5f,
        stock = 35,
    ),
    Producto(
        id = ObjectId(1.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.RAQUETA,
        marca = "DUNLOP",
        modelo = "XXL",
        precio = 20.0f,
        stock = 10,
    ),
    Producto(
        id = ObjectId(2.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.COMPLEMENTO,
        marca = "J'HAYBER",
        modelo = "Ultimate",
        precio = 9.99f,
        stock = 20,
    ),
    Producto(
        id = ObjectId(3.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.CORDAJE,
        marca = "JOMA",
        modelo = "5-7B",
        precio = 4.99f,
        stock = 100,
    ),
    Producto(
        id = ObjectId(4.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.CORDAJE,
        marca = "ADIDAS",
        modelo = "Sport",
        precio = 79.95f,
        stock = 18,
    ),
    Producto(
        id = ObjectId(5.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.RAQUETA,
        marca = "BULLPADEL",
        modelo = "2023",
        precio = 1200.0f,
        stock = 1,
    ),
    Producto(
        id = ObjectId(6.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.RAQUETA,
        marca = "DUNLOP",
        modelo = "Origin",
        precio = 12.0f,
        stock = 11,
    ),
    Producto(
        id = ObjectId(7.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.COMPLEMENTO,
        marca = "ASICS",
        modelo = "Retro",
        precio = 12.15f,
        stock = 89,
    ),
    Producto(
        id = ObjectId(8.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.CORDAJE,
        marca = "ASICS",
        modelo = "Seller",
        precio = 3.99f,
        stock = 96,
    ),
    Producto(
        id = ObjectId(9.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tipo = TipoProducto.CORDAJE,
        marca = "BABOLAT",
        modelo = "2002",
        precio = 8.75f,
        stock = 90,
    )
)

/**
 * Función que contiene una lista de Pedidos con datos ficticios de prueba.
 *
 * @return La lista de Pedidos.
 */
suspend fun getPedidosInit() = listOf(
    Pedido(
        id = ObjectId(50.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tareas = null,
        productos = listOf(getProductosInit()[2]),
        estado = TipoEstado.EN_PROCESO,
        usuario = getUsuariosInit()[0],
        fechaTope = LocalDate.of(2022, 12, 15),
        fechaEntrada = LocalDate.of(2022, 11, 10),
        fechaProgramada = LocalDate.of(2022, 12, 10),
        fechaEntrega = LocalDate.of(2022, 12, 10),
        precio = 120.0f
    ),
    Pedido(
        id = ObjectId(1.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tareas = listOf(getTareasInit()[0]),
        productos = null,
        estado = TipoEstado.EN_PROCESO,
        usuario = getUsuariosInit()[1],
        fechaTope = LocalDate.of(2022, 12, 27),
        fechaEntrada = LocalDate.of(2022, 12, 1),
        fechaProgramada = LocalDate.of(2022, 12, 20),
        fechaEntrega = LocalDate.of(2022, 12, 20),
        precio = 70.95f
    ),
    Pedido(
        id = ObjectId(2.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tareas = null,
        productos = listOf(getProductosInit()[0]),
        estado = TipoEstado.RECIBIDO,
        usuario = getUsuariosInit()[0],
        fechaTope = LocalDate.of(2023, 1, 5),
        fechaEntrada = LocalDate.of(2022, 12, 7),
        fechaProgramada = LocalDate.of(2023, 1, 4),
        fechaEntrega = LocalDate.of(2022, 1, 4),
        precio = 57.0f
    ),
    Pedido(
        id = ObjectId(3.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tareas = listOf(getTareasInit()[1], getTareasInit()[3]),
        productos = listOf(getProductosInit()[1]),
        estado = TipoEstado.TERMINADO,
        usuario = getUsuariosInit()[1],
        fechaTope = LocalDate.of(2022, 12, 1),
        fechaEntrada = LocalDate.of(2022, 11, 5),
        fechaProgramada = LocalDate.of(2022, 11, 28),
        fechaEntrega = LocalDate.of(2022, 11, 25),
        precio = 50.15f
    ),
    Pedido(
        id = ObjectId(4.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        tareas = listOf(getTareasInit()[2]),
        productos = null,
        estado = TipoEstado.TERMINADO,
        usuario = getUsuariosInit()[0],
        fechaTope = LocalDate.of(2022, 11, 14),
        fechaEntrada = LocalDate.of(2022, 9, 5),
        fechaProgramada = LocalDate.of(2022, 10, 23),
        fechaEntrega = LocalDate.of(2022, 10, 23),
        precio = 3590.72f
    )
)

/**
 * Función que contiene una lista de Tareas con datos ficticios de prueba.
 *
 * @return La lista de Tareas.
 */
suspend fun getTareasInit() = listOf(
    Tarea(
        id = ObjectId(4.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        precio = 100.0f,
        tipo = TipoTarea.ENCORDADO,
        descripcion = "tensionHorizontal = 22.5, " +
                "cordajeHorizontal = Luxilon, " +
                "tensionVertical = 22.5, " +
                "cordajeVertical = Luxilon, " +
                "nudos = 4",
        turno = getTurnosInit()[0]
    ),
    Tarea(
        id = ObjectId(1.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        precio = 150.0f,
        tipo = TipoTarea.ENCORDADO,
        descripcion = "tensionHorizontal = 26, " +
                "cordajeHorizontal = Wilson, " +
                "tensionVertical = 22.5, " +
                "cordajeVertical = Wilson, " +
                "nudos = 4",
        turno = getTurnosInit()[1]
    ),
    Tarea(
        id = ObjectId(2.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        precio = 70.0f,
        tipo = TipoTarea.PERSONALIZACION,
        descripcion = "peso = 0.3, " +
                "balance = 320.0, " +
                "rigidez = 70.0",
        turno = getTurnosInit()[2]

    ),
    Tarea(
        id = ObjectId(3.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        precio = 49.99f,
        tipo = TipoTarea.PERSONALIZACION,
        descripcion = "peso = 0.24, " +
                "balance = 330.0, " +
                "rigidez = 72.0",
        turno = getTurnosInit()[3]
    )
)

/**
 * Función que contiene una lista de Máquinas con datos ficticios de prueba.
 *
 * @return La lista de Máquinas.
 */

fun getMaquinasInit() = listOf(
    Maquina(
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
    ),
    Maquina(
        id = ObjectId(1.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        marca = "Vevor",
        modelo = "2022",
        fechaAdquisicion = LocalDate.of(2022, 5, 28),
        numeroSerie = 12,
        tipo = TipoMaquina.ENCORDAR,
        descripcion = "tipoEncordaje = MANUAL, " +
                "tensionMaxima = 30.0, " +
                "tensionMinima = 20.0"
    ),
    Maquina(
        id = ObjectId(2.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        marca = "Gamma",
        modelo = "X2",
        fechaAdquisicion = LocalDate.of(2022, 11, 5),
        numeroSerie = 10,
        tipo = TipoMaquina.PERSONALIZAR,
        descripcion = "mideManiobrabilidad = false, " +
                "balance = 400.0, " +
                "rigidez = 80.0"
    ),
    Maquina(
        id = ObjectId(3.toString().padStart(24, '0')),
        uuid = UUID.randomUUID().toString(),
        marca = "Indoostrial",
        modelo = "2600",
        fechaAdquisicion = LocalDate.of(2022, 7, 12),
        numeroSerie = 3,
        tipo = TipoMaquina.PERSONALIZAR,
        descripcion = "mideManiobrabilidad = true, " +
                "balance = 400.0, " +
                "rigidez = 80.0"
    )
)