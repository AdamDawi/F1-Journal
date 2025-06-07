package org.adamdawi.f1journal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TemperatureLapTime(
    val trackTemperature: Float,
    val avgLapTimeMs: Float
)