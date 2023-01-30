package es.dam.adp03_springmongodb.repositories.tareas

import es.dam.adp03_springmongodb.models.Tarea
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ITareasRepository: CoroutineCrudRepository<Tarea, ObjectId> {
}