package es.dam.adp03_springmongodb.services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import es.dam.adp03_springmongodb.dto.*
import es.dam.adp03_springmongodb.models.Tarea
import es.dam.adp03_springmongodb.models.Usuario
import org.bson.types.ObjectId

interface KtorFitRest {
    // Usuarios
    @GET("/users")
    suspend fun getAllUsuarios(): List<UsuarioAPIDTO>

    @GET("/users/{id}")
    suspend fun getUsuarioById(@Path("id") id: ObjectId): GetUsuarioByIdDTO

    @POST("/users")
    suspend fun createUsuario(@Body usuario: Usuario): CreateUsuarioDTO

    @PUT("/users/{id}")
    suspend fun updateUsuario(@Path("id") id: ObjectId, @Body usuario: Usuario): UpdateUsuarioDTO

    @DELETE("/users/{id}")
    suspend fun deleteUsuario(@Path("id") id: ObjectId): Unit

    // Tareas
    @GET("/todos")
    suspend fun getAllTareas(): GetAllTareasDTO

    @GET("/todos/{id}")
    suspend fun getTareaById(@Path("id") id: ObjectId): GetTareaByIdDTO

    @POST("/todos")
    suspend fun createTarea(@Body tarea: Tarea): CreateTareaDTO

    @PUT("/todos/{id}")
    suspend fun updateTarea(@Path("id") id: ObjectId, @Body tarea: Tarea): UpdateTareaDTO

    @DELETE("/todos/{id}")
    suspend fun deleteTarea(@Path("id") id: ObjectId): Unit
}