package es.dam.adp03_springmongodb.config

import es.dam.adp03_springmongodb.converters.IntegerToObjectIdConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfig {
    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters = listOf(IntegerToObjectIdConverter())
        return MongoCustomConversions(converters)
    }
}