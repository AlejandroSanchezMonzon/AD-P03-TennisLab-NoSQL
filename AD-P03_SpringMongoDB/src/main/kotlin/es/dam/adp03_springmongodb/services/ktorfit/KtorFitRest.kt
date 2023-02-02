package es.dam.adp03_springmongodb.services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import es.dam.adp03_springmongodb.dto.*
import org.bson.types.ObjectId

interface KtorFitRest {
    // Usuarios
    @GET("users")
    suspend fun getAllUsuarios(): List<UsuarioAPIDTO>

    @GET("users/{id}")
    suspend fun getUsuarioById(@Path("id") id: ObjectId): UsuarioAPIDTO

    @POST("users")
    suspend fun createUsuario(@Body usuario: UsuarioAPIDTO): UsuarioAPIDTO

    @PUT("users/{id}")
    suspend fun updateUsuario(@Path("id") id: ObjectId, @Body usuario: UsuarioAPIDTO): UsuarioAPIDTO

    @DELETE("users/{id}")
    suspend fun deleteUsuario(@Path("id") id: ObjectId): Unit

    // Tareas
    @GET("todos")
    suspend fun getAllTareas(): List<TareaAPIDTO>

    @GET("todos/{id}")
    suspend fun getTareaById(@Path("id") id: ObjectId): TareaAPIDTO

    @POST("todos")
    suspend fun createTarea(@Body tarea: TareaAPIDTO): TareaAPIDTO

    @PUT("todos/{id}")
    suspend fun updateTarea(@Path("id") id: ObjectId, @Body tarea: TareaAPIDTO): TareaAPIDTO

    @DELETE("todos/{id}")
    suspend fun deleteTarea(@Path("id") id: ObjectId): Unit
}