package org.adamdawi.f1journal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverPerformanceDifference(
    val name: String,
    val performanceDifference: Float
)