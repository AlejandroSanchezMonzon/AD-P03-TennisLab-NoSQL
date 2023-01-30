package mappers

import dto.TurnoDTO
import models.Turno

fun Turno.toTurnoDTO(): TurnoDTO {
    return TurnoDTO(
        id = id,
        uuid = uuid.toString(),
        comienzo = comienzo.toString(),
        final = final.toString(),
        maquina = maquina.uuid.toString(),
        encordador = encordador.uuid.toString()
    )
}