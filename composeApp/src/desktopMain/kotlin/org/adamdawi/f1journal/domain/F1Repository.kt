package org.adamdawi.f1journal.domain

import org.adamdawi.f1journal.domain.util.DataError
import org.adamdawi.f1journal.domain.util.Result

interface F1Repository {
    suspend fun sendF1Data(): Result<String, DataError.Network>
    suspend fun getDrivers(): Result<List<DriverAveragePosition>, DataError.Network>
}