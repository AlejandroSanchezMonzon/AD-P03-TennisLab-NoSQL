package es.dam.adp03_springmongodb.converters

import org.bson.types.ObjectId
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class IntegerToObjectIdConverter : Converter<Integer, ObjectId> {
    override fun convert(source: Integer): ObjectId {
        val string = source.toString().padStart(24, '0')
        return ObjectId(string)
    }
}

