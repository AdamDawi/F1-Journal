package org.adamdawi.f1journal.data.repository

import io.ktor.client.HttpClient
import org.adamdawi.f1journal.data.networking.get
import org.adamdawi.f1journal.data.networking.post
import org.adamdawi.f1journal.domain.model.DriverAveragePosition
import org.adamdawi.f1journal.domain.model.F1Data
import org.adamdawi.f1journal.domain.model.TemperatureLapTime
import org.adamdawi.f1journal.domain.repository.F1Repository
import org.adamdawi.f1journal.domain.util.DataError
import org.adamdawi.f1journal.domain.util.Result

class F1RepositoryImpl(
    private val httpClient: HttpClient
): F1Repository {
    override suspend fun sendF1Data(jsonData: String): Result<String, DataError.Network> {
        val result = httpClient.post<String, String>(
            route = "import/drivers",
            body = jsonData
        )

        return result
    }

    override suspend fun getF1Data(): Result<List<F1Data>, DataError.Network> {
        val result = httpClient.get<List<F1Data>>(
            route = "export/drivers"
        )
        return result
    }

    override suspend fun getDrivers(): Result<List<DriverAveragePosition>, DataError.Network> {
        val result = httpClient.get<List<DriverAveragePosition>>(
            route = "drivers"
        )
        return result
    }

    override suspend fun getTemperatureVsLapTimes(): Result<List<TemperatureLapTime>, DataError.Network> {
        val result = httpClient.get<List<TemperatureLapTime>>(
            route = "weather"
        )
        return result
    }

}