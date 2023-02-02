/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package mappers

import dto.TurnoDTO
import models.Turno

/**
 * Esta función de extensión de Turno se ocupa de convertir al objeto de tipos transferencia de datos (DTO) para poder
 * pasar la información del modelo a ficheros de una forma más sencilla, evitando así los tipos complejos.
 *
 * @return TurnoDTO, el objeto convertido en DTO.
 */
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