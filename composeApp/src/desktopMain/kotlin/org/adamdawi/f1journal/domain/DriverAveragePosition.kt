package org.adamdawi.f1journal.domain

data class DriverAveragePosition(
    val driverName: String,
    val dryAvgPosition: Float,
    val rainyAvgPosition: Float
)