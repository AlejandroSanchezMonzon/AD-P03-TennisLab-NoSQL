/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.turnos

import models.Turno
import repositories.CRUDRepository

interface ITurnosRepository: CRUDRepository<Turno, String> {
}