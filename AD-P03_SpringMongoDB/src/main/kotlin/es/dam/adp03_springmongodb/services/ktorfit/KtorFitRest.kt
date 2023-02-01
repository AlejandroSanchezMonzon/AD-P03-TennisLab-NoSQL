package es.dam.adp03_springmongodb.services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import es.dam.adp03_springmongodb.dto.*
import es.dam.adp03_springmongodb.models.Tarea
import es.dam.adp03_springmongodb.models.Usuario
import org.bson.types.ObjectId

interface KtorFitRest {
    // Usuarios
    @GET("users")
    suspend fun getAllUsuarios(): List<UsuarioAPIDTO>

    @GET("users/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): UsuarioAPIDTO

    @POST("users")
    suspend fun createUsuario(@Body usuario: Usuario): UsuarioAPIDTO

    @PUT("users/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: Usuario): UsuarioAPIDTO

    @DELETE("users/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Unit

    // Tareas
    @GET("todos")
    suspend fun getAllTareas(): List<TareaAPIDTO>

    @GET("todos/{id}")
    suspend fun getTareaById(@Path("id") id: Int): TareaAPIDTO

    @POST("todos")
    suspend fun createTarea(@Body tarea: Tarea): TareaAPIDTO

    @PUT("todos/{id}")
    suspend fun updateTarea(@Path("id") id: Int, @Body tarea: Tarea): TareaAPIDTO

    @DELETE("todos/{id}")
    suspend fun deleteTarea(@Path("id") id: Int): Unit
}