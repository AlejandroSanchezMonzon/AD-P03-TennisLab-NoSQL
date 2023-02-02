/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.usuarios

import models.Usuario
import repositories.CRUDRepository

interface IUsuariosRepository: CRUDRepository<Usuario, String> {
}