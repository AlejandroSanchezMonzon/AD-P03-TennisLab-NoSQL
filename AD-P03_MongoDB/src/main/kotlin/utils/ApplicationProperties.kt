/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package utils

import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * Método encargado de cargar el fichero application.properties.
 *
 * @return Properties, las propiedades encontradas dentro del fichero.
 */
fun readProperties(): Properties {
    val properties = Properties()
    properties.load(
        FileInputStream(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "application.properties")
    )

    return properties
}