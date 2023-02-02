/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package repositories

import kotlinx.coroutines.flow.Flow

interface CRUDRepository<T, ID> {
    suspend fun findAll(): Flow<T>
    suspend fun findById(id: ID): T?
    suspend fun save(entity: T): T
    suspend fun update(entity: T): T
    suspend fun delete(entity: T): T?
}