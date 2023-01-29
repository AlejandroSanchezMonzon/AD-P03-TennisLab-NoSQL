package repositories.maquinas

import models.Maquina
import repositories.CRUDRepository

interface IMaquinasRepository: CRUDRepository<Maquina, String> {
}