import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import repositories.usuarios.UsuariosRestRepository

fun main() = runBlocking {
    val repository = UsuariosRestRepository()

    val usuarios = repository.findAll()

    usuarios.collect {
        println(it)
    }

}