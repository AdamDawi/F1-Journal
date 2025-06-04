package org.adamdawi.f1journal.presentation.details_screen.components

import androidx.compose.runtime.Composable
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
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

    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = bottomAxis,
        ),
        modelProducer,
    )
}