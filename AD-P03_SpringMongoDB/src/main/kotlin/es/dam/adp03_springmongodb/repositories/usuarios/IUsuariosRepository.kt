package es.dam.adp03_springmongodb.repositories.usuarios

import es.dam.adp03_springmongodb.models.Usuario
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IUsuariosRepository: MongoRepository<Usuario, ObjectId> {
}