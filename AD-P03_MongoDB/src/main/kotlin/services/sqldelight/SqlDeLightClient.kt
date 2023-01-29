package services.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object SqlDeLightClient {
    private val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    init {
        AppDatabase.Schema.create(driver)
    }

    val queries = AppDatabase(driver).appDatabaseQueries

    fun removeAllData() {
        queries.transaction {
            logger.debug { "Borrando datos de la cache..." }
            queries.removeAllUsuarios()
            queries.removeAllTareas()
        }
    }
}