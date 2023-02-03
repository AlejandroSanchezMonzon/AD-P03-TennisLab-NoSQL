/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package es.dam.adp03_springmongodb.models.tareaAPI

import kotlinx.serialization.Serializable

@Serializable
data class TareaAPI(
    val id: Int,
    val completed: Boolean,
    val title: String,
    val userId: Int
)