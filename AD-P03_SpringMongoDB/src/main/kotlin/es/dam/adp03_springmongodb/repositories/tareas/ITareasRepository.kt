package es.dam.adp03_springmongodb.repositories.tareas

import es.dam.adp03_springmongodb.models.Tarea
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ITareasRepository: CoroutineCrudRepository<Tarea, ObjectId> {
}