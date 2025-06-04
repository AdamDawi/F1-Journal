package org.adamdawi.f1journal.presentation.details_screen.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import org.adamdawi.f1journal.domain.model.TemperatureLapTime

@Composable
fun TemperatureVsLapTimeChart(
    data: List<TemperatureLapTime>,
    modelProducer: CartesianChartModelProducer
) {
    val minY = remember { mutableStateOf(data.minOf { it.avgLapTimeMs } - 50.0) }
    val maxY = remember { mutableStateOf(data.maxOf { it.avgLapTimeMs } + 50.0) }

    val bottomAxis = HorizontalAxis.rememberBottom(
        title = "Avg. Temperature (°C)",
        titleComponent = TextComponent(
            padding = Insets(8.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Medium)
        )
    )

    val startAxis = VerticalAxis.rememberStart(
        title = "Avg. Lap Time (ms)",
        titleComponent = TextComponent(
            padding = Insets(8.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Medium)
        )
    )

    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                rangeProvider = CartesianLayerRangeProvider.fixed(
                    minY = minY.value,
                    maxY = maxY.value
                )
            ),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
        ),
        modelProducer,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp, top = 8.dp, start = 8.dp, end = 8.dp)
    )
}

