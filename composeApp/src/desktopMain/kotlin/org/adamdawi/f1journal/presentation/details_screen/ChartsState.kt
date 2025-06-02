package org.adamdawi.f1journal.presentation.details_screen

import org.adamdawi.f1journal.domain.DriverAveragePosition
import org.adamdawi.f1journal.domain.DriverPerformanceDifference

data class ChartsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val drivers: List<DriverAveragePosition>? = null,
    val driversDifference: List<DriverPerformanceDifference>? = null
)