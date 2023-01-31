import controllers.MongoController
import di.DiAnnotationModule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mappers.toPedidoDTO
import mappers.toProductoDTO
import mappers.toTurnoDTO
import models.TipoEstado
import models.Usuario
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import services.json.StorageJSON
import utils.logIn

var usuarioSesion: Usuario? = null

fun main() = runBlocking {
    startKoin {
        printLogger()
        modules(
            // Cambiar módulo por defecto con DiAnnotationModule.module()

            defaultModule
        )
    }
    KoinApp().run()
}

class KoinApp : KoinComponent {

    private val controller: MongoController by inject()

    suspend fun run() {
        controller()
    }

    private suspend fun controller() {
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
}
