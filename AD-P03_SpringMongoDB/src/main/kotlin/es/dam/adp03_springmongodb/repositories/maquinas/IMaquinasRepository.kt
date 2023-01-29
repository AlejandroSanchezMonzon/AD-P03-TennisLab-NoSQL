package es.dam.adp03_springmongodb.repositories.maquinas

import es.dam.adp03_springmongodb.models.Maquina
import es.dam.adp03_springmongodb.repositories.CRUDRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IMaquinasRepository: MongoRepository<Maquina, ObjectId> {
}