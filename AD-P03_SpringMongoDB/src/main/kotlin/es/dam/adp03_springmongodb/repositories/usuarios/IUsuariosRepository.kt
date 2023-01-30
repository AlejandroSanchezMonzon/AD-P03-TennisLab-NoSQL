package es.dam.adp03_springmongodb.repositories.usuarios

import es.dam.adp03_springmongodb.models.Usuario
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IUsuariosRepository: CoroutineCrudRepository<Usuario, ObjectId> {
}