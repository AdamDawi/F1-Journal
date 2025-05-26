package org.adamdawi.f1journal.data.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.adamdawi.f1journal.BuildConfig

class ApiService {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
    }

    suspend fun fetchPosts(): String {
        return try {
            val response: String = httpClient.get(BuildConfig.BASE_URL) {
                contentType(ContentType.Application.Json)
            }.toString()
            response
        } catch (e: Exception) {
            println("Error: $e")
            ""
        }
    }
}