package models.tareaAPI

import kotlinx.serialization.Serializable

@Serializable
data class TareaAPI(
    val id: Int,
    val completed: Boolean,
    val title: String,
    val userId: Int
)