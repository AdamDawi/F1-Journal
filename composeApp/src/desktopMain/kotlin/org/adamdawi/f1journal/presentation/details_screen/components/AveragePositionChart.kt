package org.adamdawi.f1journal.presentation.details_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import org.adamdawi.f1journal.domain.model.DriverAveragePosition

@Composable
fun AveragePositionChart(
    drivers: List<DriverAveragePosition>,
    modelProducer: CartesianChartModelProducer
){
    val labelMap: Map<Int, String> = drivers.mapIndexed { index, driver ->
        index to driver.driverName
    }.toMap()

    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { _, value, _ ->
            labelMap[value.toInt()] ?: ""
        }
    )

    val startAxis = VerticalAxis.rememberStart(
        title = "Avg position",
        titleComponent = TextComponent(padding = Insets(8.dp))
    )

    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = startAxis,
            bottomAxis = bottomAxis
        ),
        modelProducer
    )
    //TODO add legend to dry and rain column colors
}