package es.dam.adp03_springmongodb

import es.dam.adp03_springmongodb.repositories.usuarios.UsuariosRestRepository
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdP03SpringMongoDbApplication: CommandLineRunner {
    override fun run(vararg args: String?) = runBlocking {
        val repository = UsuariosRestRepository()

        val usuarios = repository.findAll()

        usuarios.collect {
            println(it)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<AdP03SpringMongoDbApplication>(*args)
}
