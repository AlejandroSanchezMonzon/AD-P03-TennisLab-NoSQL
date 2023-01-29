package repositories.usuarios

import models.Usuario
import repositories.CRUDRepository

interface IUsuariosRepository: CRUDRepository<Usuario, String> {
}