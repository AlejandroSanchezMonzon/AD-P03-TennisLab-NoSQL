/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

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

    /**
     * Esta función se ocupa de determinar como usuario de la sesión el usuario que ha hecho log in en el sistema.
     *
     * @param usuario Usuario que hace log in.
     */
    fun setUsuarioSesion(usuario: Usuario) {
        usuarioSesion = usuario
    }

    /**
     * Esta función llama a los métodos deleteAll() de todos los repositorios para vaciar los mismos y no dejar restos que perjudiquen el funcionamiento del programa.
     */
    suspend fun deleteAll() {
        usuariosRepository.deleteAll()
        pedidosRepository.deleteAll()
        maquinasRepository.deleteAll()
        productosRepository.deleteAll()
        tareasRepository.deleteAll()
        turnosRepository.deleteAll()
    }

    /**
     * Método encargado de recoger los datos de las funciones con datos de prueba, o en el caso de usuarios, de la API, y
     * almacenarlos en la base de datos de mongo.
     *
     * @return Unit
     */
    suspend fun descargarDatos() {
        deleteAll()

        usuariosRestRepository.findAll().collect {
            usuariosRepository.save(it)
            logger.info("save - $it")
        }
        getUsuariosInit().forEach { usuario ->
            usuariosRepository.save(usuario)
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

    /**
     * Método encargado de insertar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param maquina, El objeto, de tipo Maquina, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun guardarMaquina(maquina: Maquina) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            maquinasRepository.save(maquina)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de borrar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param maquina, El objeto, de tipo Maquina, al que se le realizará la operación.
     *
     * @return Unit
     */
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

    /**
     * Método encargado de actualizar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param maquina, El objeto, de tipo Maquina, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun actualizarMaquina(maquina: Maquina) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            maquinasRepository.save(maquina)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de buscar el objeto, cuyo identificador es el dado por parámetros, en la base
     * de datos de Mongo,si el usario tiene los permisos adecuados..
     *
     * @param id, El identificador, de tipo String, del objeto a encontar.
     *
     * @return Maquina?, El objeto que se ha encontrado de tipo Maquina. Si no se encuentra devolverá null.
     */
    suspend fun encontrarMaquina(id: ObjectId): Maquina? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            maquinasRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<Maquina>?, El flujo de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarMaquinas(): Flow<Maquina>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            maquinasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de buscar el objeto, cuyo identificador es el dado por parámetros, en la base
     * de datos de Mongo,si el usario tiene los permisos adecuados..
     *
     * @param id, El identificador, de tipo String, del objeto a encontar.
     *
     * @return Producto?, El objeto que se ha encontrado de tipo Producto. Si no se encuentra devolverá null.
     */
    suspend fun encontrarProducto(id: ObjectId): Producto? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            productosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<Producto>?, El flujo de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarProductos(): Flow<Producto>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            productosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de insertar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param producto, El objeto, de tipo Producto, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun guardarProducto(producto: Producto) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            productosRepository.save(producto)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de borrar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param producto, El objeto, de tipo Producto, al que se le realizará la operación.
     *
     * @return Unit
     */
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

    /**
     * Método encargado de actualizar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param producto, El objeto, de tipo Producto, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun actualizarProducto(producto: Producto) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            productosRepository.save(producto)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de buscar el objeto, cuyo identificador es el dado por parámetros, en la base
     * de datos de Mongo,si el usario tiene los permisos adecuados..
     *
     * @param id, El identificador, de tipo String, del objeto a encontar.
     *
     * @return Turno?, El objeto que se ha encontrado de tipo Turno. Si no se encuentra devolverá null.
     */
    suspend fun encontrarTurno(id: ObjectId): Turno? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            turnosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<Turno>?, El flujo de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarTurnos(): Flow<Turno>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            turnosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de comprobar que el turno cumple con las siguiente restriccioón:
     * el encordador asignado solo puede utilizar una máquina en un turno.
     *
     * @return Boolean, true si la cumple, false en caso contrario
     */
    suspend fun isTurnoOk(turno: Turno): Boolean {
        val turnosConElMismoEncordador = listarTurnos()?.filter { it.encordador == turno.encordador }
        val maquinaUtilizada = turnosConElMismoEncordador?.firstOrNull()?.maquina

        return turno.maquina == maquinaUtilizada
    }

    /**
     * Método encargado de insertar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param turno, El Turno, de tipo Maquina, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun guardarTurno(turno: Turno) {
        require(isTurnoOk(turno)) { "Este turno no ha podido añadirse porque el encordador que quiere asignarle ya esta utilizando otra máquina en ese turno." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            turnosRepository.save(turno)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de borrar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param turno, El objeto, de tipo Turno, al que se le realizará la operación.
     *
     * @return Unit
     */
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

    /**
     * Método encargado de actualizar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param tuurno, El objeto, de tipo Turno, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun actualizarTurno(turno: Turno) {
        require(isTurnoOk(turno)) { "Este turno no ha podido actualizarse porque el encordador que quiere asignarle ya esta utilizando otra máquina en ese turno." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            turnosRepository.save(turno)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de buscar el objeto, cuyo identificador es el dado por parámetros, en la base
     * de datos de Mongo,si el usario tiene los permisos adecuados..
     *
     * @param id, El identificador, de tipo String, del objeto a encontar.
     *
     * @return Pedido?, El objeto que se ha encontrado de tipo Pedido. Si no se encuentra devolverá null.
     */
    suspend fun encontrarPedido(id: ObjectId): Pedido? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            logger.info("Operación realizada con éxito")
            pedidosRepository.findById(id)

        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<Pedido>?, El flujo de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarPedidos(): Flow<Pedido>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            logger.info("Operación realizada con éxito")
            pedidosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    /**
     * Método encargado de comprobar que el turno cumple con las siguiente restricción:
     * el encordador asignado no puede tener más de dos pedidos asignados.
     *
     * @return Boolean, true si la cumple, false en caso contrario
     */
    //TODO: Completar
    private suspend fun isPedidoOk(pedido: Pedido): Boolean {
        //consultar tareas del pedido y consultar de la tarea el turno y del turno el encordador y no puede ser >2
        var isOverTwo = false

        val encordadoresOcupados: MutableList<Usuario>? = null
        listarPedidos()?.collect { it ->
            it.tareas?.forEach { tarea ->
                tarea.turno.encordador.let { it1 -> encordadoresOcupados?.add(it1) }
            }
        }

        val encordadoresPedidoActual: MutableList<Usuario>? = null
        pedido.tareas?.forEach { tarea ->
            tarea.turno.encordador.let { encordadoresPedidoActual?.add(it) }
        }

        return isOverTwo
    }

    /**
     * Método encargado de insertar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param pedido, El objeto, de tipo Pedido, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun guardarPedido(pedido: Pedido) {
        require(isPedidoOk(pedido)) { "Este pedido no ha podido guardarse correctamente ya que su encordador asignado ya tiene dos pedidos asignados." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.save(pedido)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de borrar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param pedido, El objeto, de tipo Pedido, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun borrarPedido(pedido: Pedido) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.delete(pedido)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de actualizar el objeto en la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @param pedidp, El objeto, de tipo Pedido, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun actualizarPedido(pedido: Pedido) {
        require(isPedidoOk(pedido)) { "Este pedido no ha podido actualizarse correctamente ya que el encordador asignado ya tiene dos pedidos asignados." }
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.save(pedido)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de insertar el objeto en la base de datos de Mongo, en la caché y en la API
     * si el usuario tiene los permisos adecuados.
     *
     * @param usuario, El objeto, de tipo Usuario, al que se le realizará la operación.
     *
     * @return Unit
     */
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

    /**
     * Método encargado de borrar el objeto en la caché, la base de datos de Mongo y en la API,
     * si el usario tiene los permisos adecuados.
     *
     * @param usuario, El objeto, de tipo Usuario, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun borrarUsuario(usuario: Usuario) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.filter { it.usuario == usuario }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a este usuario. " }

            require(listarTurnos()?.filter { it.encordador == usuario }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el turno/s asociados a este usuario. " }

            usuariosRepository.delete(usuario)
            usuariosCacheRepository.delete(usuario)
            usuariosRestRepository.delete(usuario)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de actualizar el objeto en la caché, en la base de datos de Mongo y en la API,
     * si el usario tiene los permisos adecuados.
     *
     * @param usuario, El objeto, de tipo Usuario, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun actualizarUsuario(usuario: Usuario) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            usuariosRepository.save(usuario)
            usuariosCacheRepository.update(usuario)
            usuariosRestRepository.update(usuario)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }


    suspend fun encontrarUsuario(id: ObjectId): Usuario? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            try {
                usuariosCacheRepository.findById(id.toString())
            } catch (e: Exception) {
                logger.error { "Usuario con id: $id no encontrado en la cache." }
                usuariosRepository.findById(id)
            }
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<List<Usuario>>?, El flujo de la lista de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarUsuarios(): Flow<Usuario>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            usuariosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de insertar el objeto en la base de datos de Mongo y en la API,
     * si el usario tiene los permisos adecuados.
     *
     * @param tarea, El objeto, de tipo Tarea, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun guardarTarea(tarea: Tarea) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.save(tarea)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de borrar el objeto en la base de datos de Mongo y de la API,
     * si el usario tiene los permisos adecuados.
     *
     * @param tarea, El objeto, de tipo Tarea, al que se le realizará la operación.
     *
     * @return Unit
     */
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

    /**
     * Método encargado de actualizar el objeto en la base de datos de Mongo y en la API,
     * si el usario tiene los permisos adecuados.
     *
     * @param tarea, El objeto, de tipo Tarea, al que se le realizará la operación.
     *
     * @return Unit
     */
    suspend fun actualizarTarea(tarea: Tarea) {
        if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.update(tarea)
            logger.info("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<Tarea>?, El flujo de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarTarea(): Flow<Tarea>? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            tareasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            return null
        }
    }

    /**
     * Método encargado de buscar el objeto, cuyo identificador es el dado por parámetros, en la base
     * de datos de Mongo y si no lo encuentra en la API,si el usario tiene los permisos adecuados.
     *
     * @param id, El identificador, de tipo String, del objeto a encontar.
     *
     * @return Tarea?, El objeto que se ha encontrado de tipo Tarea. Si no se encuentra devolverá null.
     */
    suspend fun encontrarTarea(id: ObjectId): Tarea? {
        return if (usuarioSesion.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion.rol == TipoUsuario.ADMIN_ENCARGADO) {
            if (tareasRepository.findById(id) == null) {
                logger.info("Operación realizada con éxito")
                tareasRestRepository.findById(id)
            } else {
                logger.info("Operación realizada con éxito")
                tareasRepository.findById(id)
            }
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }
}