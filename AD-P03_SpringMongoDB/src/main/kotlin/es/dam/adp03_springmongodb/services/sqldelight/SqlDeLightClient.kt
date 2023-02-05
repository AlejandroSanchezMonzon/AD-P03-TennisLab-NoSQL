/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.services.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.AppDatabase
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
object SqlDeLightClient {
    private val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    init {
        AppDatabase.Schema.create(driver)
    }

    val queries = AppDatabase(driver).appDatabaseQueries

    fun removeAllData() {
        queries.transaction {
            logger.info { "Borrando datos de la cache..." }
            queries.deleteAllUsuarios()
        }
    }
}