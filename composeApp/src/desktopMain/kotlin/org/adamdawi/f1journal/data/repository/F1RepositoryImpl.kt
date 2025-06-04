package org.adamdawi.f1journal.data.repository

import io.ktor.client.HttpClient
import org.adamdawi.f1journal.domain.model.DriverAveragePosition
import org.adamdawi.f1journal.domain.repository.F1Repository
import org.adamdawi.f1journal.domain.model.TemperatureLapTime
import org.adamdawi.f1journal.domain.util.DataError
import org.adamdawi.f1journal.domain.util.Result

class F1RepositoryImpl(
    private val httpClient: HttpClient
): F1Repository {
    override suspend fun sendF1Data(): Result<String, DataError.Network> {
//        val result = httpClient.post<MoviesBasedOnMovieResponse>(
//            route = "movie/$movieId/recommendations",
//            queryParameters = mapOf(
//                "language" to "en-US",
//                "page" to page.toString(),
//            )
//        )

        return Result.Error(DataError.Network.SERVER_ERROR)
    }

    override suspend fun getDrivers(): Result<List<DriverAveragePosition>, DataError.Network> {
        return Result.Success(listOf(
            DriverAveragePosition("Verstappen", 1.3f, 1.1f),
            DriverAveragePosition("Hamilton", 3.0f, 2.5f),
            DriverAveragePosition("Leclerc", 4.2f, 5.1f),
            DriverAveragePosition("Norris", 5.4f, 3.9f),
            DriverAveragePosition("Sainz", 5.9f, 6.0f),
            DriverAveragePosition("Perez", 6.5f, 7.2f),
            DriverAveragePosition("Russell", 7.1f, 6.8f),
            DriverAveragePosition("Alonso", 8.0f, 7.5f),
            DriverAveragePosition("Ocon", 9.2f, 9.0f),
            DriverAveragePosition("Gasly", 10.4f, 10.1f),
            DriverAveragePosition("Zhou", 11.5f, 11.3f),
            DriverAveragePosition("Tsunoda", 12.0f, 12.4f),
            DriverAveragePosition("Schumacher", 13.1f, 13.2f),
            DriverAveragePosition("Magnussen", 14.3f, 13.7f),
            DriverAveragePosition("Latifi", 15.2f, 15.4f),
            DriverAveragePosition("Albon", 16.0f, 16.1f),
            DriverAveragePosition("Stroll", 17.5f, 18.0f),
            DriverAveragePosition("Ricciardo", 18.2f, 17.9f),
            DriverAveragePosition("Vettel", 19.1f, 19.3f)
        ))
    }

    override suspend fun getTemperatureVsLapTimes(): Result<List<TemperatureLapTime>, DataError.Network> {
        return Result.Success(listOf(
            TemperatureLapTime(20f, 1100f),
            TemperatureLapTime(22f, 1050f),
            TemperatureLapTime(24f, 1030f),
            TemperatureLapTime(26f, 1010f),
            TemperatureLapTime(28f, 1005f),
            TemperatureLapTime(30f, 1000f),
        ))
    }

}