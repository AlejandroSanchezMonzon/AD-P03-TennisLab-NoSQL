package repositories.tareas

import models.Tarea
import repositories.CRUDRepository

interface ITareasRepository: CRUDRepository<Tarea, String> {
}