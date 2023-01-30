package es.dam.adp03_springmongodb.repositories.maquinas

import es.dam.adp03_springmongodb.models.Maquina
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IMaquinasRepository: CoroutineCrudRepository<Maquina, ObjectId> {
}