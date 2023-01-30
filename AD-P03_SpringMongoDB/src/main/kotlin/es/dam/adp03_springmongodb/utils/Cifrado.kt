package es.dam.adp03_springmongodb.utils

import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

fun cifrarPassword(password: String): String {
    return Hashing.sha256()
        .hashString(password, StandardCharsets.UTF_8)
        .toString()
}