package services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import models.Tarea
import models.Usuario

interface KtorFitRest {
    // Usuarios
    @GET("/users")
    suspend fun getAllUsuarios(): GetAllUsuariosDTO

    @GET("/users/{id}")
    suspend fun getUsuarioById(@Path("id") id: String): GetUsuarioByIdDTO

    @POST("/users")
    suspend fun createUusario(@Body usuario: Usuario): CreateUsuarioDTO

    @PUT("/users/{id}")
    suspend fun updateUsuario(@Path("id") id: String, @Body usuario: Usuario): UpdateUsuarioDTO

    @DELETE("/users/{id}")
    suspend fun deleteUsuario(@Path("id") id: String): Unit

    // Tareas
    @GET("/todos")
    suspend fun getAllTareas(): GetAllTareasDTO

    @GET("/todos/{id}")
    suspend fun getTareaById(@Path("id") id: String): GetTareaByIdDTO

    @POST("/todos")
    suspend fun createTarea(@Body tarea: Tarea): CreateTareaDTO

    @PUT("/todos/{id}")
    suspend fun updateTarea(@Path("id") id: String, @Body tarea: Tarea): UpdateTareaDTO

    @DELETE("/todos/{id}")
    suspend fun deleteTarea(@Path("id") id: String): Unit
}