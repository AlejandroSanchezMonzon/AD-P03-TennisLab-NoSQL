package es.dam.adp03_springmongodb.controllers

import es.dam.adp03_springmongodb.db.*
import es.dam.adp03_springmongodb.models.*
import es.dam.adp03_springmongodb.repositories.maquinas.IMaquinasRepository
import es.dam.adp03_springmongodb.repositories.pedidos.IPedidosRepository
import es.dam.adp03_springmongodb.repositories.productos.IProductosRepository
import es.dam.adp03_springmongodb.repositories.tareas.ITareasRepository
import es.dam.adp03_springmongodb.repositories.tareas.TareasRestRepository
import es.dam.adp03_springmongodb.repositories.turnos.ITurnosRepository
import es.dam.adp03_springmongodb.repositories.usuarios.IUsuariosRepository
import es.dam.adp03_springmongodb.repositories.usuarios.UsuariosCacheRepository
import es.dam.adp03_springmongodb.repositories.usuarios.UsuariosRestRepository
import kotlinx.coroutines.flow.*
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

private val logger = KotlinLogging.logger { }

@Controller
class MongoController
@Autowired constructor(
    private val maquinasRepository: IMaquinasRepository,
    private val pedidosRepository: IPedidosRepository,
    private val productosRepository: IProductosRepository,
    private val tareasRepository: ITareasRepository,
    private val tareasRestRepository: TareasRestRepository,
    private val turnosRepository: ITurnosRepository,
    private val usuariosRepository: IUsuariosRepository,
    private val usuariosRestRepository: UsuariosRestRepository,
    private val usuariosCacheRepository: UsuariosCacheRepository
) {
    private lateinit var usuarioSesion: Usuario

    fun setUsuarioSesion(usuario: Usuario) {
        usuarioSesion = usuario
    }

    suspend fun deleteAll() {
        usuariosRepository.deleteAll()
        pedidosRepository.deleteAll()
        maquinasRepository.deleteAll()
        productosRepository.deleteAll()
        tareasRepository.deleteAll()
        turnosRepository.deleteAll()
    }

    suspend fun descargarDatos() {
        deleteAll()

        usuariosRestRepository.findAll().collect {
            usuariosRepository.save(it)
            logger.info("save - $it")
        }
        getUsuariosInit().forEach { usuario ->
            usuariosRepository.save(usuario)
//            usuariosCacheRepository.save(usuario)
            logger.info("save - $usuario")
        }
        getMaquinasInit().forEach { maquina ->
            maquinasRepository.save(maquina)
            logger.info("save - $maquina")
        }
        getProductosInit().forEach { producto ->
            productosRepository.save(producto)
            logger.info("save - $producto")
        }
        getTurnosInit().forEach { turno ->
            turnosRepository.save(turno)
            logger.info("save - $turno")
        }
        getTareasInit().forEach { tarea ->
            tareasRepository.save(tarea)
            logger.info("save - $tarea")
        }
        getPedidosInit().forEach { pedido ->
            pedidosRepository.save(pedido)
            logger.info("save - $pedido")
        }
    }

    suspend fun guardarMaquina(maquina: Maquina) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            maquinasRepository.save(maquina)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarMaquina(maquina: Maquina) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarTurnos()?.filter { it.maquina == maquina }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el turno/s asociados a esta máquina. " }
            maquinasRepository.delete(maquina)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarMaquina(maquina: Maquina) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            maquinasRepository.save(maquina)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarMaquina(id: ObjectId): Maquina? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            maquinasRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarMaquinas(): Flow<Maquina>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            maquinasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun encontrarProducto(id: ObjectId): Producto? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            productosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarProductos(): Flow<Producto>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            productosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun guardarProducto(producto: Producto) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            productosRepository.save(producto)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarProducto(producto: Producto) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.map { it.productos?.contains(producto) }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a este producto." }
            productosRepository.delete(producto)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarProducto(producto: Producto) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            productosRepository.save(producto)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarTurno(id: ObjectId): Turno? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            turnosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarTurnos(): Flow<Turno>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            turnosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun isTurnoOk(turno: Turno): Boolean {
        //consultar turnos y comprobar que su encordador solo utiliza esa maquina
        val turnosConElMismoEncordador = listarTurnos()?.filter { it.encordador == turno.encordador }
        val maquinaUtilizada = turnosConElMismoEncordador?.firstOrNull()?.maquina

        return turno.maquina == maquinaUtilizada
    }

    suspend fun guardarTurno(turno: Turno) {
        require(isTurnoOk(turno)) { "Este turno no ha podido añadirse porque el encordador que quiere asignarle ya esta utilizando otra máquina en ese turno." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            turnosRepository.save(turno)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarTurno(turno: Turno) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarTarea()?.filter { it.turno == turno }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice la tarea/s asociados a este turno. " }
            turnosRepository.delete(turno)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarTurno(turno: Turno) {
        require(isTurnoOk(turno)) { "Este turno no ha podido actualizarse porque el encordador que quiere asignarle ya esta utilizando otra máquina en ese turno." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            turnosRepository.save(turno)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarPedido(id: ObjectId): Pedido? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            logger.info("Operación realizada con éxito")
            pedidosRepository.findById(id)

        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    suspend fun listarPedidos(): Flow<Pedido>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            logger.info("Operación realizada con éxito")
            pedidosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    private suspend fun isPedidoOk(pedido: Pedido): Boolean {
        //consultar tareas del pedido y consultar de la tarea el turno y del turno el encordador y no puede ser >2
        var isOverTwo = false

        val encordadoresOcupados: MutableList<Usuario>? = null
        listarPedidos()?.collect { it ->
            it.tareas?.forEach { tarea ->
                tarea.turno.encordador?.let { it1 -> encordadoresOcupados?.add(it1) }
            }
        }

        val encordadoresPedidoActual: MutableList<Usuario>? = null
        pedido.tareas?.forEach { tarea ->
            tarea.turno.encordador?.let { encordadoresPedidoActual?.add(it) }
        }

        return isOverTwo
    }

    suspend fun guardarPedido(pedido: Pedido) {
        require(isPedidoOk(pedido)) { "Este pedido no ha podido guardarse correctamente ya que su encordador asignado ya tiene dos pedidos asignados." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.save(pedido)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarPedido(pedido: Pedido) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.delete(pedido)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarPedido(pedido: Pedido) {
        require(isPedidoOk(pedido)) { "Este pedido no ha podido actualizarse correctamente ya que el encordador asignado ya tiene dos pedidos asignados." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.save(pedido)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun guardarUsuario(usuario: Usuario) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            usuariosRepository.save(usuario)
            usuariosCacheRepository.save(usuario)
            usuariosRestRepository.save(usuario)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarUsuario(usuario: Usuario) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.filter { it.usuario == usuario }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a este usuario. " }

            require(listarTurnos()?.filter { it.encordador == usuario }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el turno/s asociados a este usuario. " }

            usuariosCacheRepository.delete(usuario)
            usuariosRepository.delete(usuario)
            usuariosRestRepository.delete(usuario)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            usuariosCacheRepository.update(usuario)
            usuariosRepository.save(usuario)
            usuariosRestRepository.update(usuario)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    //TODO la cache no devuelve nulo nunca
    suspend fun encontrarUsuario(id: ObjectId): Usuario? {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            return usuariosCacheRepository.findById(id.toString())
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    fun listarUsuarios(): Flow<Usuario>? {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            return usuariosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    suspend fun guardarTarea(tarea: Tarea) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.save(tarea)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarTarea(tarea: Tarea) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.map { it.tareas?.contains(tarea) }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a esta tarea." }

            tareasRepository.delete(tarea)
            tareasRestRepository.delete(tarea)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarTarea(tarea: Tarea) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.update(tarea)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun listarTarea(): Flow<Tarea>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            tareasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    //TODO la cache no devuelve nulo nunca
    suspend fun encontrarTarea(id: ObjectId): Tarea? {
        var tareaEncontrada: Tarea? = null
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            if (tareasRepository.findById(id) == null) {
                logger.info("Operación realizada con éxito")
                return tareasRestRepository.findById(id)
            } else {
                logger.info("Operación realizada con éxito")
                return tareasRepository.findById(id)
            }
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }
}