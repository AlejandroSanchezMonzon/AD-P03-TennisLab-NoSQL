package es.dam.adp03_springmongodb.mappers

import es.dam.adp03_springmongodb.dto.TurnoDTO
import es.dam.adp03_springmongodb.models.Turno

fun Turno.toTurnoDTO(): TurnoDTO {
    return TurnoDTO(
        id = id.toString(),
        uuid = uuid,
        comienzo = comienzo.toString(),
        final = final.toString(),
        maquina = maquina.uuid,
        encordador = encordador.uuid
    )
}