package controllers

import db.*
import kotlinx.coroutines.flow.*
import models.*
import mu.KotlinLogging
import repositories.maquinas.MaquinasRepository
import repositories.pedidos.PedidosRepository
import repositories.productos.ProductosRepository
import repositories.tareas.TareasRepository
import repositories.tareas.TareasRestRepository
import repositories.turnos.TurnosRepository
import repositories.usuarios.UsuariosCacheRepository
import repositories.usuarios.UsuariosRepository
import repositories.usuarios.UsuariosRestRepository
import usuarioSesion

val logger = KotlinLogging.logger {  }

class MongoController(

    private val maquinasRepository: MaquinasRepository,
    private val pedidosRepository: PedidosRepository,
    private val productosRepository: ProductosRepository,
    private val tareasRepository: TareasRepository,
    private val tareasRestRepository: TareasRestRepository,
    private val turnosRepository: TurnosRepository,
    private val usuariosRepository: UsuariosRepository,
    private val usuariosRestRepository: UsuariosRestRepository,
    private val usuariosCacheRepository: UsuariosCacheRepository
) {

    suspend fun descargarDatos(){
        usuariosRestRepository.findAll().collect{
            logger.info("save - $it")
            usuariosRepository.save(it)
        }
        getMaquinasInit().forEach { maquina ->
            logger.info("save - $maquina")
            maquinasRepository.save(maquina)
        }
        getProductosInit().forEach { producto ->
            logger.info("save - $producto")
            productosRepository.save(producto)
        }
        getTurnosInit().forEach { turno ->
            logger.info("save - $turno")
            turnosRepository.save(turno)
        }
        getTareasInit().forEach { tarea ->
            logger.info("save - $tarea")
            tareasRepository.save(tarea)
        }

        getPedidosInit().forEach { pedido ->
            logger.info("save - $pedido")
            pedidosRepository.save(pedido)
        }
    }

    suspend fun guardarMaquina(maquina: Maquina) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            maquinasRepository.save(maquina)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarMaquina(maquina: Maquina) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            require(listarTurnos()?.filter { it.maquina == maquina }?.count() == 0)
            {"Antes de realizar la operación, elimine o actualice el turno/s asociados a esta máquina. "}
            maquinasRepository.delete(maquina)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarMaquina(maquina: Maquina) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            maquinasRepository.update(maquina)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarMaquina(id: String): Maquina? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            maquinasRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarMaquinas(): Flow<Maquina>? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            maquinasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun encontrarProducto(id: String): Producto? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            productosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarProductos(): Flow<Producto>? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            productosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun guardarProducto(producto: Producto) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            productosRepository.save(producto)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarProducto(producto: Producto) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            require(listarPedidos()?.map { it.productos?.contains(producto) }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a este producto." }

            productosRepository.delete(producto)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarProducto(producto: Producto) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            productosRepository.update(producto)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarTurno(id: String): Turno? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            turnosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarTurnos(): Flow<Turno>? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
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
        require(isTurnoOk(turno)) {"Este turno no ha podido añadirse porque el encordador que quiere asignarle ya esta utilizando otra máquina en ese turno."}
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            turnosRepository.save(turno)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarTurno(turno: Turno) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            require(listarTarea()?.filter { it.turno == turno }?.count() == 0)
            {"Antes de realizar la operación, elimine o actualice la tarea/s asociados a este turno. "}
            turnosRepository.delete(turno)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarTurno(turno: Turno) {
        require(isTurnoOk(turno)) {"Este turno no ha podido actualizarse porque el encordador que quiere asignarle ya esta utilizando otra máquina en ese turno."}
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            turnosRepository.update(turno)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarPedido(id: String): Pedido? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR){
            logger.debug("Operación realizada con éxito")
            pedidosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarPedidos(): Flow<Pedido>? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR){
            logger.debug("Operación realizada con éxito")
            pedidosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    private suspend fun isPedidoOk(pedido: Pedido): Boolean {
        //consultar tareas del pedido y consultar de la tarea el turno y del turno el encordador y no puede ser >2
        var isOverTwo = false

        val encordadoresOcupados : MutableList<Usuario>? = null
        listarPedidos()?.collect { it ->
            it.tareas?.forEach { tarea ->
                encordadoresOcupados?.add(tarea.turno.encordador)
            }
        }

        val encordadoresPedidoActual: MutableList<Usuario>? = null
        pedido.tareas?.forEach { tarea ->
            encordadoresPedidoActual?.add(tarea.turno.encordador)
        }

        return isOverTwo
    }

    suspend fun guardarPedido(pedido: Pedido) {
        require(isPedidoOk(pedido)) {"Este pedido no ha podido guardarse correctamente ya que su encordador asignado ya tiene dos pedidos asignados."}
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR){
            pedidosRepository.save(pedido)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarPedido(pedido: Pedido) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR){
            pedidosRepository.delete(pedido)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarPedido(pedido: Pedido) {
        require(isPedidoOk(pedido)) {"Este pedido no ha podido actualizarse correctamente ya que el encordador asignado ya tiene dos pedidos asignados."}
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR){
            pedidosRepository.update(pedido)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun guardarUsuario(usuario: Usuario) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            usuariosRepository.save(usuario)
            usuariosCacheRepository.save(usuario)
            usuariosRestRepository.save(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarUsuario(usuario: Usuario) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            require(listarPedidos()?.filter { it.usuario == usuario }?.count() == 0)
            {"Antes de realizar la operación, elimine o actualice el pedido/s asociados a este usuario. "}

            require(listarTurnos()?.filter { it.encordador == usuario }?.count() == 0)
            {"Antes de realizar la operación, elimine o actualice el turno/s asociados a este usuario. "}

            usuariosCacheRepository.delete(usuario)
            usuariosRepository.delete(usuario)
            usuariosRestRepository.delete(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            usuariosCacheRepository.update(usuario)
            usuariosRepository.update(usuario)
            usuariosRestRepository.update(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    //TODO la cache no devuelve nulo nunca
    suspend fun encontrarUsuario(id: String): Usuario? {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            if(usuariosCacheRepository.findById(id.toLong()) == null){
                if(usuariosRepository.findById(id) == null){
                    if(usuariosRestRepository.findById(id) == null){
                        logger.error("No se ha encontrado el usuario.")
                        return null
                    }else{
                        logger.debug("Operación realizada con éxito")
                        return usuariosRestRepository.findById(id)
                    }
                } else{
                    logger.debug("Operación realizada con éxito")
                    return usuariosRepository.findById(id)
                }
            }else{
                logger.debug("Operación realizada con éxito")
                return usuariosCacheRepository.findById(id.toLong())
            }
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

     fun listarUsuarios(): Flow<List<Usuario>>? {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            return usuariosCacheRepository.findAll()
        }else{
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    suspend fun guardarTarea(tarea: Tarea) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.save(tarea)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarTarea(tarea: Tarea) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.map { it.tareas?.contains(tarea) }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a esta tarea." }

            tareasRepository.delete(tarea)
            tareasRestRepository.delete(tarea)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarTarea(tarea: Tarea) {
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            tareasRepository.update(tarea)
            tareasRestRepository.update(tarea)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun listarTarea(): Flow<Tarea>? {
        return if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            tareasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    //TODO la cache no devuelve nulo nunca
    suspend fun encontrarTarea(id: String): Tarea? {
        var tareaoEncontrado: Tarea? = null
        if(usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO){
            if(tareasRepository.findById(id) == null) {
                if(tareasRestRepository.findById(id)== null) {
                    logger.error("No se ha encontrado el usuario.")
                    return null
                } else {
                    logger.debug("Operación realizada con éxito")
                    return tareasRestRepository.findById(id)
                }
            } else {
                logger.debug("Operación realizada con éxito")
                return tareasRepository.findById(id) }
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }
}