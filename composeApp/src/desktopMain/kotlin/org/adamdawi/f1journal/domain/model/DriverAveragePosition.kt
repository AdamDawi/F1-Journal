package org.adamdawi.f1journal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverAveragePosition(
    val driverName: String,
    val drvAvgPosition: Float,
    val rainyAvgPosition: Float
)