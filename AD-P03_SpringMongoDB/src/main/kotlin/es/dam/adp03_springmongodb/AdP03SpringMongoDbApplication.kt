package es.dam.adp03_springmongodb

import es.dam.adp03_springmongodb.controllers.MongoController
import es.dam.adp03_springmongodb.mappers.toPedidoDTO
import es.dam.adp03_springmongodb.mappers.toProductoDTO
import es.dam.adp03_springmongodb.mappers.toTurnoDTO
import es.dam.adp03_springmongodb.models.TipoEstado
import es.dam.adp03_springmongodb.models.Usuario
import es.dam.adp03_springmongodb.services.json.StorageJSON
import es.dam.adp03_springmongodb.utils.logIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

var usuarioSesion: Usuario? = null
@SpringBootApplication
class AdP03SpringMongoDbApplication(
    @Autowired
    private val controller : MongoController
): CommandLineRunner {
    override fun run(vararg args: String?) = runBlocking {
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

fun main(args: Array<String>) {
    runApplication<AdP03SpringMongoDbApplication>(*args)
}
