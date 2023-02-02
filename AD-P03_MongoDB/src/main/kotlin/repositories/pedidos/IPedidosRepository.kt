/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories.pedidos

import models.Pedido
import repositories.CRUDRepository

interface IPedidosRepository: CRUDRepository<Pedido, String> {
}