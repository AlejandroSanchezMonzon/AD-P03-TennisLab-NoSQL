package es.dam.adp03_springmongodb.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document("maquinas")
@Serializable
data class Maquina(
    @Id @Contextual
    val id: ObjectId = ObjectId.get(),
    @Contextual
    val uuid: String = UUID.randomUUID().toString(),
    val marca: String,
    val modelo: String,
    @Contextual
    val fechaAdquisicion: LocalDate,
    val numeroSerie: Long,
    val tipo: TipoMaquina,
    val descripcion: String
)

enum class TipoMaquina {
    PERSONALIZAR, ENCORDAR
}