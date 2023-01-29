package es.dam.adp03_springmongodb.repositories.maquinas

import models.Maquina
import es.dam.adp03_springmongodb.repositories.CRUDRepository

interface IMaquinasRepository: CRUDRepository<Maquina, String> {
}