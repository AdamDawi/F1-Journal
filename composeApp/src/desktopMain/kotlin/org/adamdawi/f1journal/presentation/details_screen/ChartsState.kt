package org.adamdawi.f1journal.presentation.details_screen

import org.adamdawi.f1journal.domain.model.DriverAveragePosition
import org.adamdawi.f1journal.domain.model.DriverPerformanceDifference
import org.adamdawi.f1journal.domain.model.TemperatureLapTime

data class ChartsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val drivers: List<DriverAveragePosition>? = null,
    val driversDifference: List<DriverPerformanceDifference>? = null,
    val temperatureLapTime: List<TemperatureLapTime>? = null,
    val isDriversDataLoading: Boolean = true,
    val isTemperatureLapTimeDataLoading: Boolean = true
)