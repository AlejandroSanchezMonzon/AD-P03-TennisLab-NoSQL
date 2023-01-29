package es.dam.adp03_springmongodb.repositories.turnos

import es.dam.adp03_springmongodb.models.Turno
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ITurnosRepository: MongoRepository<Turno, ObjectId> {
}