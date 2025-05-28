package org.adamdawi.f1journal.data.repository

import io.ktor.client.HttpClient
import org.adamdawi.f1journal.domain.F1Repository
import org.adamdawi.f1journal.domain.util.DataError
import org.adamdawi.f1journal.domain.util.Result

class F1RepositoryImpl(
    private val httpClient: HttpClient
): F1Repository {
    override fun sendF1Data(): Result<String, DataError.Network> {
//        val result = httpClient.post<MoviesBasedOnMovieResponse>(
//            route = "movie/$movieId/recommendations",
//            queryParameters = mapOf(
//                "language" to "en-US",
//                "page" to page.toString(),
//            )
//        )

        return Result.Error(DataError.Network.SERVER_ERROR)
    }

}