/**
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 */
package services.ktorfit

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import mu.KotlinLogging

object KtorFitClient {
    private const val API_URL = "https://jsonplaceholder.typicode.com/"

    private val ktorfit by lazy {
        Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(Json { isLenient = true; ignoreUnknownKeys = true })
                }
                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }
            .baseUrl(API_URL)
            .build()
    }

    val instance by lazy {
        ktorfit.create<KtorFitRest>()
    }
}