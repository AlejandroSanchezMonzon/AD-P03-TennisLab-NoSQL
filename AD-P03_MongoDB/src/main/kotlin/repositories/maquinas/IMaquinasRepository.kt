/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.maquinas

import models.Maquina
import repositories.CRUDRepository

interface IMaquinasRepository: CRUDRepository<Maquina, String> {
}