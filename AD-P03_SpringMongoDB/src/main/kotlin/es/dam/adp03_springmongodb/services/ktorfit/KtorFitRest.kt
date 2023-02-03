/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import es.dam.adp03_springmongodb.dto.*
import org.bson.types.ObjectId

interface KtorFitRest {
    // Usuarios

    /**
     * Método encargado de acceder a la API REST para encontrar una lista de todos los objetos que hay
     * de tipo UsuarioAPIDTO.
     *
     * @return List<Usuario>, la lista de objetos encontrados.
     */
    @GET("users")
    suspend fun getAllUsuarios(): List<UsuarioAPIDTO>

    /**
     * Método encargado de acceder a la API REST para encontrar una usuario cuyo id sea dado por parámetros.
     *
     * @param id, El identidicador, tipo String, del usuario a encontrar.
     *
     * @return UsuarioAPIDTO, el usuario encontrado con los atributos que tiene en la API REST.
     */
    @GET("users/{id}")
    suspend fun getUsuarioById(@Path("id") id: ObjectId): UsuarioAPIDTO

    /**
     * Método encargado de acceder a la API REST para insertar un usuario dado por parámetros.
     *
     * @param usuario, El usuario, tipo Usuario, a insertar.
     *
     * @return UsuarioAPIDTO, el usuario insertado con los atributos que tiene en la API REST.
     */
    @POST("users")
    suspend fun createUsuario(@Body usuario: UsuarioAPIDTO): UsuarioAPIDTO

    /**
     * Método encargado de acceder a la API REST para actualizar un usuario cuyo id y demás atributos
     * son dados por parámetros.
     *
     * @param id,  El identidicador, tipo String, del usuario a actualizar.
     * @param usuario, El usuario, tipo Usuario, a insertar.
     *
     * @return UsuarioAPIDTO, el usuario insertado con los atributos que tiene en la API REST.
     */
    @PUT("users/{id}")
    suspend fun updateUsuario(@Path("id") id: ObjectId, @Body usuario: UsuarioAPIDTO): UsuarioAPIDTO

    /**
     * Método encargado de acceder a la API REST para borrar el usuario cuyo id es dado por parámetros.
     *
     * @param id, El identidicador, tipo String, del usuario a borrar.
     *
     * @return Unit
     */
    @DELETE("users/{id}")
    suspend fun deleteUsuario(@Path("id") id: ObjectId): Unit

    // Tareas

    /**
     * Método encargado de API REST de datos para encontrar una lista de todos los objetos que hay
     * de tipo TareaAPIDTO .
     *
     * @return List<Tarea>, la lista de objetos encontrados.
     */
    @GET("todos")
    suspend fun getAllTareas(): List<TareaAPIDTO>

    /**
     * Método encargado de acceder a la API REST para encontrar una tarea cuyo id sea dado por parámetros.
     *
     * @param id, El identidicador, tipo String, del usuario a encontrar.
     *
     * @return TareaAPIDTO, la tarea encontrada con los atributos que tiene en la API REST.
     */
    @GET("todos/{id}")
    suspend fun getTareaById(@Path("id") id: ObjectId): TareaAPIDTO

    /**
     * Método encargado de acceder a la API REST para insertar una tarea dado por parámetros.
     *
     * @param tarea, La tarea, tipo Tarea, a insertar.
     *
     * @return TareaAPIDTO, la tarea insertada con los atributos que tiene en la API REST.
     */
    @POST("todos")
    suspend fun createTarea(@Body tarea: TareaAPIDTO): TareaAPIDTO

    /**
     * Método encargado de acceder a la API REST para actualizar una tarea cuyo id y demás
     * atributos son dados por parámetros.
     *
     * @param id,  El identidicador, tipo String, del usuario a actualizar.
     * @param tarea, El usuario, tipo Tarea, a insertar.
     *
     * @return TareaAPIDTO, la tarea insertado con los atributos que tiene en la API REST.
     */
    @PUT("todos/{id}")
    suspend fun updateTarea(@Path("id") id: ObjectId, @Body tarea: TareaAPIDTO): TareaAPIDTO

    /**
     * Método encargado de acceder a la API REST para borrar la tarea cuyo id es dado por parámetros.
     *
     * @param id, El identidicador, tipo String, de la tarea a borrar.
     *
     * @return Unit
     */
    @DELETE("todos/{id}")
    suspend fun deleteTarea(@Path("id") id: ObjectId): Unit
}