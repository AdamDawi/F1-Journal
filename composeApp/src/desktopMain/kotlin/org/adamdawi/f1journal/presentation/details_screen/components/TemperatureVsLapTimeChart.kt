package org.adamdawi.f1journal.presentation.details_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import org.adamdawi.f1journal.domain.model.TemperatureLapTime

@Composable
fun TemperatureVsLapTimeChart(
    data: List<TemperatureLapTime>,
    modelProducer: CartesianChartModelProducer
) {


    LaunchedEffect(Unit) {

    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                rangeProvider = CartesianLayerRangeProvider.fixed(
                    minY = data.minOf { it.avgLapTimeMs } - 50.0,
                    maxY = data.maxOf { it.avgLapTimeMs } + 50.0
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ),
        modelProducer,
    )
}

