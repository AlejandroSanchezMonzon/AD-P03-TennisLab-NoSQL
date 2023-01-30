package es.dam.adp03_springmongodb.controllers

import es.dam.adp03_springmongodb.db.getMaquinasInit
import es.dam.adp03_springmongodb.db.getProductosInit
import es.dam.adp03_springmongodb.db.getTareasInit
import es.dam.adp03_springmongodb.db.getTurnosInit
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
import es.dam.adp03_springmongodb.utils.cifrarPassword
import es.dam.adp03_springmongodb.utils.randomUserType
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller

val logger = KotlinLogging.logger {  }

@Controller
class MongoController
@Autowired constructor(
    @Value("usuarioSesion")
    private val usuarioSesion: Usuario,
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

    suspend fun descargarDatos(){
        usuariosRestRepository.findAll().collect{ usuario ->
            usuario.rol = randomUserType()
            usuario.password = cifrarPassword(usuario.nombre)
            usuariosRepository.save(usuario)
        }
        getMaquinasInit().forEach { maquina -> maquinasRepository.save(maquina) }
        getProductosInit().forEach { producto -> productosRepository.save(producto) }
        getTurnosInit(usuariosRepository).forEach { turno -> turnosRepository.save(turno) }
        getTareasInit(usuariosRepository).forEach { tarea -> tareasRepository.save(tarea) }
        getProductosInit().forEach { producto -> productosRepository.save(producto)}
    }

    suspend fun guardarMaquina(maquina: Maquina) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            maquinasRepository.save(maquina)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarMaquina(maquina: Maquina) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            maquinasRepository.delete(maquina)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarMaquina(maquina: Maquina) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            maquinasRepository.save(maquina)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarMaquina(id: String): Maquina? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            maquinasRepository.findById(ObjectId(id))
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarMaquinas(): Flow<Maquina>? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            maquinasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun encontrarProducto(id: String): Producto? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            productosRepository.findById(ObjectId(id))
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarProductos(): Flow<Producto>? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            productosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun guardarProducto(producto: Producto) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            productosRepository.save(producto)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarProducto(producto: Producto) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            productosRepository.delete(producto)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarProducto(producto: Producto) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            productosRepository.save(producto)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarTurno(id: String): Turno? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            turnosRepository.findById(ObjectId(id))
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarTurnos(): Flow<Turno>? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            turnosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun guardarTurno(turno: Turno) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            turnosRepository.save(turno)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarTurno(turno: Turno) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            turnosRepository.delete(turno)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarTurno(turno: Turno) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            turnosRepository.save(turno)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun encontrarPedido(id: String): Pedido? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR){
            logger.debug("Operación realizada con éxito")
            pedidosRepository.findById(ObjectId(id))
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun listarPedidos(): Flow<Pedido>? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR){
            logger.debug("Operación realizada con éxito")
            pedidosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    suspend fun guardarPedido(pedido: Pedido) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO  || usuarioSesion.rol == TipoUsuario.ENCORDADOR){
            pedidosRepository.save(pedido)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarPedido(pedido: Pedido) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR){
            pedidosRepository.delete(pedido)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarPedido(pedido: Pedido) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR){
            pedidosRepository.save(pedido)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun guardarUsuario(usuario: Usuario) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            usuariosRepository.save(usuario)
            usuariosCacheRepository.save(usuario)
            usuariosRestRepository.save(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarUsuario(usuario: Usuario) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            usuariosCacheRepository.delete(usuario)
            usuariosRepository.delete(usuario)
            usuariosRestRepository.delete(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            usuariosCacheRepository.update(usuario)
            usuariosRepository.save(usuario)
            usuariosRestRepository.update(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    //TODO la cache no devuelve nulo nunca
    suspend fun encontrarUsuario(id: String): Usuario? {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            if(usuariosCacheRepository.findById(id.toLong()) == null){
                if(usuariosRepository.findById(ObjectId(id)) == null){
                    if(usuariosRestRepository.findById(ObjectId(id)) == null){
                        logger.error("No se ha encontrado el usuario.")
                        return null
                    }else{
                        logger.debug("Operación realizada con éxito")
                        return usuariosRestRepository.findById(ObjectId(id))
                    }
                } else{
                    logger.debug("Operación realizada con éxito")
                    return usuariosRepository.findById(ObjectId(id))
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
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            return usuariosCacheRepository.findAll()
        }else{
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    suspend fun guardarTarea(tarea: Tarea) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.save(tarea)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun borrarTarea(tarea: Tarea) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.delete(tarea)
            tareasRestRepository.delete(tarea)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun actualizarTarea(tarea: Tarea) {
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            tareasRepository.save(tarea)
            tareasRestRepository.update(tarea)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    suspend fun listarTarea(): Flow<Tarea>? {
        return if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            logger.debug("Operación realizada con éxito")
            tareasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    //TODO la cache no devuelve nulo nunca
    suspend fun encontrarTarea(id: String): Tarea? {
        var tareaEncontrada: Tarea? = null
        if(usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO){
            if(tareasRepository.findById(ObjectId(id)) == null) {
                if(tareasRestRepository.findById(ObjectId(id))== null) {
                    logger.error("No se ha encontrado el usuario.")
                    return null
                } else {
                    logger.debug("Operación realizada con éxito")
                    return tareasRestRepository.findById(ObjectId(id))
                }
            } else {
                logger.debug("Operación realizada con éxito")
                return tareasRepository.findById(ObjectId(id)) }
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }
}