/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.repositories.turnos

import es.dam.adp03_springmongodb.models.Turno
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ITurnosRepository : CoroutineCrudRepository<Turno, ObjectId> {
}