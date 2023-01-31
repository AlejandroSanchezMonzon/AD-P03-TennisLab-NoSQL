package es.dam.adp03_springmongodb.utils

import java.io.File
import java.io.FileInputStream
import java.util.*

fun readProperties(): Properties {
    val properties = Properties()
    properties.load(
        FileInputStream(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "application.properties")
    )

    return properties
}