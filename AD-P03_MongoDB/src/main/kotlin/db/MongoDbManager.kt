/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package db

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import mu.KotlinLogging
import org.litote.kmongo.KMongo
import utils.readProperties


private val logger = KotlinLogging.logger {}

object MongoDbManager {
    private  var mongoClient: MongoClient
    var database: MongoDatabase

    private val properties = readProperties()

    private val MONGOHOST = properties.getProperty("MONGO_HOST")
    private val MONGOPASS = properties.getProperty("MONGO_INITDB_ROOT_PASSWORD")
    private val MONGOUSER = properties.getProperty("MONGO_INITDB_ROOT_USERNAME")
    private val MONGOBBDD = properties.getProperty("MONGO_BBDD")
    private val MONGO_OPTIONS = properties.getProperty("MONGO_OPTIONS")
    private val MONGO_OPTIONS_WINDOWS = properties.getProperty("MONGO_OPTIONS_WINDOWS")

    init {
        logger.debug("Inicializando conexión a la base de datos")

        // Cadena de conexión en LOCAL para MAC: "mongodb://$MONGOUSER:$MONGOPASS@$MONGOHOST/$MONGOBBDD?$MONGO_OPTIONS"
        // Cadena de conexión en LOCAL para Windows: "mongodb://$MONGOHOST/$MONGOBBDD?$MONGO_OPTIONS_WINDOWS"
        mongoClient = KMongo.createClient("mongodb://$MONGOHOST/$MONGOBBDD?$MONGO_OPTIONS_WINDOWS")

        database = mongoClient.getDatabase(MONGOBBDD)
    }
}

