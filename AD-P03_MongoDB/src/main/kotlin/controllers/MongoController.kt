/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package controllers

import db.*
import kotlinx.coroutines.flow.*
import models.*
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
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
import java.util.*

val logger = KotlinLogging.logger { }

@Single
class MongoController(
    @Named("MaquinasRepository")
    private val maquinasRepository: MaquinasRepository,
    @Named("PedidosRepository")
    private val pedidosRepository: PedidosRepository,
    @Named("ProductosRepository")
    private val productosRepository: ProductosRepository,
    @Named("TareasRepository")
    private val tareasRepository: TareasRepository,
    @Named("TareasRestRepository")
    private val tareasRestRepository: TareasRestRepository,
    @Named("TurnosRepository")
    private val turnosRepository: TurnosRepository,
    @Named("UsuariosRepository")
    private val usuariosRepository: UsuariosRepository,
    @Named("UsuariosRestRepository")
    private val usuariosRestRepository: UsuariosRestRepository,
    @Named("UsuariosCacheRepository")
    private val usuariosCacheRepository: UsuariosCacheRepository
) {


    /**
     * Método encargado de recoger los datos de las funciones con datos de prueba, o en el caso de usuarios, de la API, y
     * almacenarlos en la base de datos de mongo.
     *
     * @return Unit
     */
    suspend fun descargarDatos() {
        borrarDatos()
        usuariosRestRepository.findAll().collect {
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

    private fun borrarDatos() {
        usuariosRepository.deleteAll()
        maquinasRepository.deleteAll()
        productosRepository.deleteAll()
        turnosRepository.deleteAll()
        tareasRepository.deleteAll()
        pedidosRepository.deleteAll()
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            val prueba = maquinasRepository.save(maquina)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarTurnos()?.filter { it.maquina == maquina }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el turno/s asociados a esta máquina. " }
            maquinasRepository.delete(maquina)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            maquinasRepository.update(maquina)
            logger.debug("Operación realizada con éxito")
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
    suspend fun encontrarMaquina(id: String): Maquina? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
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
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
            return maquinasRepository.findAll()
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
    suspend fun encontrarProducto(id: String): Producto? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
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
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            productosRepository.save(producto)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.map { it.productos?.contains(producto) }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a este producto." }

            productosRepository.delete(producto)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            productosRepository.update(producto)
            logger.debug("Operación realizada con éxito")
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
    suspend fun encontrarTurno(id: String): Turno? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
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
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            turnosRepository.save(turno)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarTarea()?.filter { it.turno == turno }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice la tarea/s asociados a este turno. " }
            turnosRepository.delete(turno)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            turnosRepository.update(turno)
            logger.debug("Operación realizada con éxito")
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
    suspend fun encontrarPedido(id: String): Pedido? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR) {
            logger.debug("Operación realizada con éxito")
            pedidosRepository.findById(id)
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de listar los objetos, de un tipo específico, de la base de datos de Mongo,
     * si el usario tiene los permisos adecuados.
     *
     * @return Flow<Pedido>?, El flujo de objetos encontrados. Si no se encuentra devolverá null.
     */
    suspend fun listarPedidos(): Flow<Pedido>? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR) {
            logger.debug("Operación realizada con éxito")
            pedidosRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
        }
    }

    /**
     * Método encargado de comprobar que el turno cumple con las siguiente restricción:
     * el encordador asignado no puede tener más de dos pedidos asignados.
     *
     * @return Boolean, true si la cumple, false en caso contrario
     */
    private suspend fun isPedidoOk(pedido: Pedido): Boolean {
        return if (pedido.tareas == null) {
            true
        } else {
            val usuarios =
                listarPedidos()!!.toList().flatMap { it.tareas!! }.map { it.turno }
                    .groupBy { it.encordador }
                    .filter { (_, turno) -> turno.size >= 2 }
                    .map { it.key }

            pedido.tareas.any {
                usuarios.contains(it.turno.encordador)
            }
        }
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
        require(pedido.tareas != null || pedido.productos != null) { "El pedido no se puede realizar, su contenido está vacío." }
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.save(pedido)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.delete(pedido)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO || usuarioSesion?.rol == TipoUsuario.ENCORDADOR) {
            pedidosRepository.update(pedido)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            usuariosRepository.save(usuario)
            usuariosCacheRepository.save(usuario)
            usuariosRestRepository.save(usuario)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.filter { it.usuario == usuario }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a este usuario. " }

            require(listarTurnos()?.filter { it.encordador == usuario }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el turno/s asociados a este usuario. " }

            usuariosCacheRepository.delete(usuario)
            usuariosRepository.delete(usuario)
            usuariosRestRepository.delete(usuario)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            usuariosCacheRepository.update(usuario)
            usuariosRepository.update(usuario)
            usuariosRestRepository.update(usuario)
            logger.debug("Operación realizada con éxito")
        } else {
            logger.error("No está autorizado a realizar esta operación.")
        }
    }

    /**
     * Método encargado de buscar el objeto, cuyo identificador es el dado por parámetros, en la caché,
     * si no lo encuentra en la base de datos de Mongo y si no lo encuentra en la API
     * ,si el usario tiene los permisos adecuados.
     *
     * @param id, El identificador, de tipo String, del objeto a encontar.
     *
     * @return Maquina?, El objeto que se ha encontrado de tipo Maquina. Si no se encuentra devolverá null.
     */
    suspend fun encontrarUsuario(id: String): Usuario? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.info("Operación realizada con éxito")
            try {
                usuariosCacheRepository.findById(id.toLong())
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
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.save(tarea)
            tareasRestRepository.save(tarea)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            require(listarPedidos()?.map { it.tareas?.contains(tarea) }?.count() == 0)
            { "Antes de realizar la operación, elimine o actualice el pedido/s asociados a esta tarea." }

            tareasRepository.delete(tarea)
            tareasRestRepository.delete(tarea)
            logger.debug("Operación realizada con éxito")
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
        if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            tareasRepository.update(tarea)
            tareasRestRepository.update(tarea)
            logger.debug("Operación realizada con éxito")
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
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
            logger.debug("Operación realizada con éxito")
            tareasRepository.findAll()
        } else {
            logger.error("No está autorizado a realizar esta operación.")
            null
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
    suspend fun encontrarTarea(id: String): Tarea? {
        return if (usuarioSesion?.rol == TipoUsuario.ADMIN_JEFE || usuarioSesion?.rol == TipoUsuario.ADMIN_ENCARGADO) {
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