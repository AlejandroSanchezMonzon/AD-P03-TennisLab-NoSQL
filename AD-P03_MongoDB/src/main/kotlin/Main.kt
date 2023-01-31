import controllers.MongoController
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mappers.toPedidoDTO
import mappers.toProductoDTO
import mappers.toTurnoDTO
import models.TipoEstado
import models.Usuario
import repositories.maquinas.MaquinasRepository
import repositories.pedidos.PedidosRepository
import repositories.productos.ProductosRepository
import repositories.tareas.TareasRepository
import repositories.tareas.TareasRestRepository
import repositories.turnos.TurnosRepository
import repositories.usuarios.UsuariosCacheRepository
import repositories.usuarios.UsuariosRepository
import repositories.usuarios.UsuariosRestRepository
import services.json.StorageJSON
import services.sqldelight.SqlDeLightClient
import utils.logIn

var usuarioSesion: Usuario? = null
fun main() = runBlocking {
    val controller = MongoController(
        MaquinasRepository(),
        PedidosRepository(),
        ProductosRepository(),
        TareasRepository(),
        TareasRestRepository(),
        TurnosRepository(),
        UsuariosRepository(),
        UsuariosRestRepository(),
        UsuariosCacheRepository(SqlDeLightClient)
    )

    val serviceJSON = StorageJSON()

    controller.descargarDatos()
    usuarioSesion  = logIn()

    //Información completa en JSON de un pedido.
    val pedido = controller.encontrarPedido("1")!!.toPedidoDTO()
    serviceJSON.writePedido("Informacion_completa_pedido", listOf(pedido))

    //Listado de pedidos pendientes en JSON.
    val pedidosPendientes = controller.listarPedidos()?.toList()
        ?.filter { it.estado == TipoEstado.EN_PROCESO }
        ?.map{it.toPedidoDTO()}
    serviceJSON.writePedido("listado_pedidos_pendientes", pedidosPendientes!!)

    //Listado de pedidos completados en JSON.
    val pedidosCompletados = controller.listarPedidos()?.toList()
        ?.filter { it.estado == TipoEstado.TERMINADO }
        ?.map { it.toPedidoDTO() }
    serviceJSON.writePedido("listado_pedidos_completados", pedidosCompletados!!)

    //Listado de productos y servicios que ofrecemos en JSON.
    val productos = controller.listarProductos()?.toList()
        ?.map { it.toProductoDTO() }
    serviceJSON.writeProducto("productos_ofrecidos", productos!!)

    //Listado de asignaciones para los encordadores por fecha en JSON.
    val asignaciones =  controller.listarTurnos()?.toList()
        ?.sortedBy { it.comienzo }
        ?.map { it.toTurnoDTO() }
    serviceJSON.writeTurno("listado_asignaciones_encordadores_por_fecha", asignaciones!!)

}