package org.adamdawi.f1journal.presentation.details_screen

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import org.koin.compose.viewmodel.koinViewModel

data class DetailsScreen(val id: Int): Screen {

    enum class ChartType(val displayName: String) {
        AVG_POSITION("Avg drivers position in weather"),
        PERFORMANCE_DIFFERENCE("Difference in performance"),
    }

    @Composable
    override fun Content() {
        val viewModel: ChartsViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        var selectedChart by remember { mutableStateOf(ChartType.AVG_POSITION) }
        var expanded by remember { mutableStateOf(false) }


        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(Unit) {
            loadChartData(modelProducer, drivers)
        }

        val modelProducer2 = remember { CartesianChartModelProducer() }

        LaunchedEffect(Unit) {
            modelProducer2.runTransaction {
                columnSeries {
                    // Populate the chart with performance differences
                    sortedPerformanceDifferences.forEachIndexed { index, driver ->
                        // Map each driver to a unique X position (index)
                        series(index.toDouble(), driver.performanceDifference)
                    }
                }
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

                // Tu mogą być odstępy np. Spacer(modifier = Modifier.height(16.dp))

                when (selectedChart) {
                    ChartType.AVG_POSITION -> {
                        val labelMap: Map<Int, String> = drivers.mapIndexed { index, driver ->
                            index to driver.driverName
                        }.toMap()

                        val bottomAxis = HorizontalAxis.rememberBottom(
                            valueFormatter = { _, value, _ ->
                                labelMap[value.toInt()] ?: ""
                            }
                        )

                        CartesianChartHost(
                            rememberCartesianChart(
                                rememberColumnCartesianLayer(),
                                startAxis = VerticalAxis.rememberStart(),
                                bottomAxis = bottomAxis,
                            ),
                            modelProducer,
                        )
                    }

                    ChartType.PERFORMANCE_DIFFERENCE -> {
                        val labelMap2 = sortedPerformanceDifferences.mapIndexed { index, driver ->
                            index.toDouble() to driver.name
                        }.toMap()

                        val bottomAxis2 = HorizontalAxis.rememberBottom(
                            valueFormatter = { _, value, _ ->
                                labelMap2[value] ?: ""
                            }
                        )

                        CartesianChartHost(
                            rememberCartesianChart(
                                rememberColumnCartesianLayer(),
                                startAxis = VerticalAxis.rememberStart(),
                                bottomAxis = bottomAxis2,
                            ),
                            modelProducer2,
                        )
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
    }
}

val drivers = listOf(
    DriverAveragePosition("Verstappen", 1.3f, 1.1f),
    DriverAveragePosition("Hamilton", 3.0f, 2.5f),
    DriverAveragePosition("Leclerc", 4.2f, 5.1f),
    DriverAveragePosition("Norris", 5.4f, 3.9f),
    DriverAveragePosition("Sainz", 5.9f, 6.0f),
    DriverAveragePosition("Perez", 6.5f, 7.2f),
    DriverAveragePosition("Russell", 7.1f, 6.8f),
    DriverAveragePosition("Alonso", 8.0f, 7.5f),
    DriverAveragePosition("Ocon", 9.2f, 9.0f),
    DriverAveragePosition("Gasly", 10.4f, 10.1f),
    DriverAveragePosition("Zhou", 11.5f, 11.3f),
    DriverAveragePosition("Tsunoda", 12.0f, 12.4f),
    DriverAveragePosition("Schumacher", 13.1f, 13.2f),
    DriverAveragePosition("Magnussen", 14.3f, 13.7f),
    DriverAveragePosition("Latifi", 15.2f, 15.4f),
    DriverAveragePosition("Albon", 16.0f, 16.1f),
    DriverAveragePosition("Stroll", 17.5f, 18.0f),
    DriverAveragePosition("Ricciardo", 18.2f, 17.9f),
    DriverAveragePosition("Vettel", 19.1f, 19.3f)
)

val performanceDifferences = drivers.map { driver ->
    val difference = driver.dryAvgPosition - driver.rainyAvgPosition
    DriverPerformanceDifference(driver.driverName, difference)
}

// Sorting the list to show who has the biggest positive difference (i.e., improvement in the rain)
val sortedPerformanceDifferences = performanceDifferences.sortedByDescending { it.performanceDifference }



data class DriverAveragePosition(
    val driverName: String,
    val dryAvgPosition: Float,
    val rainyAvgPosition: Float
)

data class DriverPerformanceDifference(
    val name: String,
    val performanceDifference: Float
)