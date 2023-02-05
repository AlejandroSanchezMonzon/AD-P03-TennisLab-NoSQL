/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */

package es.dam.adp03_springmongodb.config

import es.dam.adp03_springmongodb.converters.IntegerToObjectIdConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfig {
    /**
     * Este método se encarga de añadir a la configuración de Spring el convertidor personalizado que hemos creado.
     */
    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters = listOf(IntegerToObjectIdConverter())
        return MongoCustomConversions(converters)
    }
}