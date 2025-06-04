package org.adamdawi.f1journal.presentation.details_screen

import AveragePositionChart
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.common.data.ExtraStore
import org.adamdawi.f1journal.domain.model.DriverAveragePosition
import org.adamdawi.f1journal.domain.model.DriverPerformanceDifference
import org.adamdawi.f1journal.domain.model.TemperatureLapTime
import org.adamdawi.f1journal.presentation.components.ErrorScreen
import org.adamdawi.f1journal.presentation.components.LoadingScreen
import org.adamdawi.f1journal.presentation.details_screen.DetailsScreen.ChartType
import org.adamdawi.f1journal.presentation.details_screen.components.PerformanceDifferenceChart
import org.adamdawi.f1journal.presentation.details_screen.components.TemperatureVsLapTimeChart
import org.koin.compose.viewmodel.koinViewModel

data class DetailsScreen(val id: Int) : Screen {
    enum class ChartType(val displayName: String) {
        AVG_POSITION("Average Position (Dry vs Rain)"),
        PERFORMANCE_DIFFERENCE("Performance Difference (Dry - Rain)"),
        TEMP_VS_LAP_TIME("Temperature vs Avg Lap Time")
    }

    @Composable
    override fun Content() {
        val viewModel: ChartsViewModel = koinViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        when{
            state.value.isLoading -> LoadingScreen()
            state.value.error != null -> ErrorScreen(message = state.value.error)
            else -> {
                ChartsContent(
                    state = state.value
                )
            }
        }
    }
}
val legendKey = ExtraStore.Key<List<String>>()
@Composable
fun ChartsContent(
    state: ChartsState
) {
    val navigator = LocalNavigator.currentOrThrow
    var selectedChart by remember { mutableStateOf(ChartType.AVG_POSITION) }
    var expanded by remember { mutableStateOf(false) }

    val modelProducer = remember { CartesianChartModelProducer() }
    val modelProducer2 = remember { CartesianChartModelProducer() }
    val modelProducer3 = remember { CartesianChartModelProducer() }


    LaunchedEffect(state.drivers) {
        state.drivers?.let {
            loadChartData(modelProducer, it)
        }
        state.driversDifference?.let {
            loadChartData2(modelProducer2, it)
        }
        state.temperatureLapTime?.let {
            loadChartData3(modelProducer3, it)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = { Text("Charts") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
                    .zIndex(1f)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Choose chart:")
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(onClick = { expanded = true }) {
                            Text(selectedChart.displayName)
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ChartType.entries.forEach { chart ->
                            DropdownMenuItem(onClick = {
                                selectedChart = chart
                                expanded = false
                            }) {
                                Text(chart.displayName)
                            }
                        }
                    }
                }
            }

            when (selectedChart) {
                ChartType.AVG_POSITION -> {
                    if (state.drivers != null) {
                        AveragePositionChart(
                            drivers = state.drivers,
                            modelProducer = modelProducer
                        )
                    } else {
                        Text("No data")
                    }
                }

                ChartType.PERFORMANCE_DIFFERENCE -> {
                    if (state.driversDifference != null) {
                        PerformanceDifferenceChart(
                            driversDifference = state.driversDifference,
                            modelProducer = modelProducer2
                        )
                    } else {
                        Text("No data")
                    }
                }

                ChartType.TEMP_VS_LAP_TIME -> {
                    if (state.temperatureLapTime != null) {
                        TemperatureVsLapTimeChart(
                            data = state.temperatureLapTime,
                            modelProducer = modelProducer3
                        )
                    } else {
                        Text("No data")
                    }
                }
            }
        }
    }
}

suspend fun loadChartData(
    modelProducer: CartesianChartModelProducer,
    data: List<DriverAveragePosition>
) {
    modelProducer.runTransaction {
        columnSeries {
            series(data.map { it.dryAvgPosition })
            series(data.map { it.rainyAvgPosition })
        }
        extras { it[legendKey] = listOf("Dry", "Rain") }
    }
}

suspend fun loadChartData2(
    modelProducer: CartesianChartModelProducer,
    data: List<DriverPerformanceDifference>
) {
    modelProducer.runTransaction {
        columnSeries {
            series(data.map { it.performanceDifference })
        }
    }
}

suspend fun loadChartData3(
    modelProducer: CartesianChartModelProducer,
    data: List<TemperatureLapTime>
) {
    modelProducer.runTransaction {
        lineSeries {
            series(
                data.map { it.trackTemperature }, data.map { it.avgLapTimeMs }
            )
        }
    }
}