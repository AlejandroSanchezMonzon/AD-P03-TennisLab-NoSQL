package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.util.*

@Serializable
data class Maquina(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val marca: String,
    val modelo: String,
    @Contextual
    val fechaAdquisicion: LocalDate,
    val numeroSerie: Int,
    val tipo: TipoMaquina
)

enum class TipoMaquina {
    PERSONALIZAR, ENCORDAR
}