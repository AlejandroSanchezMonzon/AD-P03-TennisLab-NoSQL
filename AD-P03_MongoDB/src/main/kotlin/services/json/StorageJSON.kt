package services.json

import dto.PedidoDTO
import dto.ProductoDTO
import dto.TurnoDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

class StorageJSON {
    /**
     * Método que escribe, mediante una lista de DTOs de Pedidos, un fichero en formato JSON con la información de cada uno de ellos.
     *
     * @param nombreArchivo El nombre con el que se va nombrar el arcivo JSON que se cree seguido de la extensión .json.
     * @param entityDTO Lista de DTOs de la cual sacaremos, para cada uno, la información la cual escribiremos en el JSON.
     *
     * @return Unit, no devolveremos nada, pero llamamos a los métodos para la creación y escritura del JSON.
     */
    fun writePedido(nombreArchivo: String, entityDTO: List<PedidoDTO>) {
        logger.info("Escribiendo JSON.")
        val directorio = System.getProperty("user.dir") +
                File.separator + "src" +
                File.separator + "main" +
                File.separator + "resources"
        val fichero = File(directorio + File.separator + "$nombreArchivo.json")
        val json = Json { prettyPrint = true }
        fichero.writeText(json.encodeToString(entityDTO))
    }

    /**
     * Método que escribe, mediante una lista de DTOs de Productos, un fichero en formato JSON con la información de cada uno de ellos.
     *
     * @param nombreArchivo El nombre con el que se va nombrar el arcivo JSON que se cree seguido de la extensión .json.
     * @param entityDTO Lista de DTOs de la cual sacaremos, para cada uno, la información la cual escribiremos en el JSON.
     *
     * @return Unit, no devolveremos nada, pero llamamos a los métodos para la creación y escritura del JSON.
     */
    fun writeProducto(nombreArchivo: String, entityDTO: List<ProductoDTO>) {
        logger.info("Escribiendo JSON.")
        val directorio = System.getProperty("user.dir") +
                File.separator + "src" +
                File.separator + "main" +
                File.separator + "resources"
        val fichero = File(directorio + File.separator + "$nombreArchivo.json")
        val json = Json { prettyPrint = true }
        fichero.writeText(json.encodeToString(entityDTO))
    }

    /**
     * Método que escribe, mediante una lista de DTOs de Turnos, un fichero en formato JSON con la información de cada uno de ellos.
     *
     * @param nombreArchivo El nombre con el que se va nombrar el arcivo JSON que se cree seguido de la extensión .json.
     * @param entityDTO Lista de DTOs de la cual sacaremos, para cada uno, la información la cual escribiremos en el JSON.
     *
     * @return Unit, no devolveremos nada, pero llamamos a los métodos para la creación y escritura del JSON.
     */
    fun writeTurno(nombreArchivo: String, entityDTO: List<TurnoDTO>) {
        logger.info("Escribiendo JSON.")
        val directorio = System.getProperty("user.dir") +
                File.separator + "src" +
                File.separator + "main" +
                File.separator + "resources"
        val fichero = File(directorio + File.separator + "$nombreArchivo.json")
        val json = Json { prettyPrint = true }
        fichero.writeText(json.encodeToString(entityDTO))
    }
}