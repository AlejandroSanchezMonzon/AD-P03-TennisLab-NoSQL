package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDate
import java.util.*

@Serializable
data class Maquina(
    @BsonId
    val id: String = newId<Maquina>().toString(),
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