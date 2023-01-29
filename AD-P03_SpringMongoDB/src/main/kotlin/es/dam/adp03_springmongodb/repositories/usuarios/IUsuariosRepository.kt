package es.dam.adp03_springmongodb.repositories.usuarios

import models.Usuario
import es.dam.adp03_springmongodb.repositories.CRUDRepository

interface IUsuariosRepository: CRUDRepository<Usuario, String> {
}