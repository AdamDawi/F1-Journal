package org.adamdawi.f1journal.domain.repository

import org.adamdawi.f1journal.domain.model.DriverAveragePosition
import org.adamdawi.f1journal.domain.model.F1Data
import org.adamdawi.f1journal.domain.model.TemperatureLapTime
import org.adamdawi.f1journal.domain.util.DataError
import org.adamdawi.f1journal.domain.util.Result

interface F1Repository {
    suspend fun sendF1Data(jsonData: String): Result<String, DataError.Network>
    suspend fun getF1Data(): Result<List<F1Data>, DataError.Network>
    suspend fun getDrivers(): Result<List<DriverAveragePosition>, DataError.Network>
    suspend fun getTemperatureVsLapTimes(): Result<List<TemperatureLapTime>, DataError.Network>
}