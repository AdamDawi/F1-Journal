package org.adamdawi.f1journal.presentation.details_screen.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import org.adamdawi.f1journal.domain.model.DriverPerformanceDifference

@Composable
fun PerformanceDifferenceChart(
    driversDifference: List<DriverPerformanceDifference>,
    modelProducer: CartesianChartModelProducer
){
    val labelMap: Map<Int, String> =
        driversDifference.mapIndexed { index, driver ->
            index to driver.name
        }.toMap()
    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { _, value, _ ->
            labelMap[value.toInt()] ?: ""
        }
    )

    val startAxis = VerticalAxis.rememberStart(
        title = "Avg. Position Difference (Dry − Rain)",
        titleComponent = TextComponent(
            padding = Insets(8.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Medium)
        )
    )

    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
        ),
        modelProducer,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp, top = 8.dp, start = 8.dp, end = 8.dp)
    )
}