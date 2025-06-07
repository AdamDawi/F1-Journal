package org.adamdawi.f1journal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class F1Data(
    val lapTimeList: List<String> = listOf(),
    val name: String,
    val raceList: List<String> = listOf(),
    val resultList: List<Result> = listOf()
)

@Serializable
data class Result(
    val position: Int,
    val raceName: String,
    val raceTime: String
)