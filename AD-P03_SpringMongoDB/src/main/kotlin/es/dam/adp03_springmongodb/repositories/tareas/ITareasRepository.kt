package es.dam.adp03_springmongodb.repositories.tareas

import models.Tarea
import es.dam.adp03_springmongodb.repositories.CRUDRepository

interface ITareasRepository: CRUDRepository<Tarea, String> {
}