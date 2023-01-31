import controllers.MongoController
import kotlinx.coroutines.runBlocking
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

    controller.descargarDatos()
    usuarioSesion = logIn()

}