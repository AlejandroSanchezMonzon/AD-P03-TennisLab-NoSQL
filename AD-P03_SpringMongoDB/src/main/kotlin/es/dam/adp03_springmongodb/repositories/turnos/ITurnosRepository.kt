package es.dam.adp03_springmongodb.repositories.turnos

import models.Turno
import es.dam.adp03_springmongodb.repositories.CRUDRepository

interface ITurnosRepository: CRUDRepository<Turno, String> {
}