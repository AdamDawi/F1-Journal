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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import org.adamdawi.f1journal.domain.DriverAveragePosition
import org.adamdawi.f1journal.domain.DriverPerformanceDifference
import org.adamdawi.f1journal.presentation.details_screen.components.AveragePositionChart
import org.koin.compose.viewmodel.koinViewModel

data class DetailsScreen(val id: Int): Screen {

    enum class ChartType(val displayName: String) {
        AVG_POSITION("Average Position (Dry vs Rain)"),
        PERFORMANCE_DIFFERENCE("Performance Difference (Dry - Rain)")
    }

    @Composable
    override fun Content() {
        val viewModel: ChartsViewModel = koinViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.currentOrThrow
        var selectedChart by remember { mutableStateOf(ChartType.AVG_POSITION) }
        var expanded by remember { mutableStateOf(false) }

        val modelProducer = remember { CartesianChartModelProducer() }
        val modelProducer2 = remember { CartesianChartModelProducer() }

        LaunchedEffect(state.value.drivers) {
            state.value.drivers?.let {
                loadChartData(modelProducer, it)
            }
            state.value.driversDifference?.let {
                loadChartData2(modelProducer2, it)
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
                        if(state.value.drivers != null) {
                            AveragePositionChart(drivers = state.value.drivers!!, modelProducer = modelProducer)
                        }else{
                            Text("No data")
                        }
                    }

                    ChartType.PERFORMANCE_DIFFERENCE -> {
                        if(state.value.driversDifference != null){
                            val labelMap: Map<Int, String> = state.value.driversDifference!!.mapIndexed { index, driver ->
                                index to driver.name
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
                                modelProducer2,
                            )
                        }else{
                            Text("No data")
                        }
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