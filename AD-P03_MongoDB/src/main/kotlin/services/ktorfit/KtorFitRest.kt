package services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import dto.*
import models.Tarea
import models.Usuario

interface KtorFitRest {
    // Usuarios
    @GET("users")
    suspend fun getAllUsuarios(): List<UsuarioAPIDTO>

    @GET("users/{id}")
    suspend fun getUsuarioById(@Path("id") id: String): UsuarioAPIDTO

    @POST("users")
    suspend fun createUsuario(@Body usuario: UsuarioAPIDTO): UsuarioAPIDTO

    @PUT("users/{id}")
    suspend fun updateUsuario(@Path("id") id: String, @Body usuario: UsuarioAPIDTO): UsuarioAPIDTO

    @DELETE("users/{id}")
    suspend fun deleteUsuario(@Path("id") id: String): Unit

    // Tareas
    @GET("todos")
    suspend fun getAllTareas(): List<TareaAPIDTO>

    @GET("todos/{id}")
    suspend fun getTareaById(@Path("id") id: String): TareaAPIDTO

    @POST("todos")
    suspend fun createTarea(@Body tarea: TareaAPIDTO): TareaAPIDTO

    @PUT("todos/{id}")
    suspend fun updateTarea(@Path("id") id: String, @Body tarea: TareaAPIDTO): TareaAPIDTO

    @DELETE("todos/{id}")
    suspend fun deleteTarea(@Path("id") id: String): Unit
}