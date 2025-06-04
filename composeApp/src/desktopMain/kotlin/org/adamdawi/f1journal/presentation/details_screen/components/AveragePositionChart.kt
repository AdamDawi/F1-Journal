import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.LegendItem
import com.patrykandpatrick.vico.multiplatform.common.component.ShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberLineComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import org.adamdawi.f1journal.domain.model.DriverAveragePosition
import org.adamdawi.f1journal.presentation.details_screen.legendKey

@Composable
fun AveragePositionChart(
    drivers: List<DriverAveragePosition>,
    modelProducer: CartesianChartModelProducer
) {
    val labelMap: Map<Int, String> = drivers.mapIndexed { index, driver ->
        index to driver.driverName
    }.toMap()
    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { _, value, _ ->
            labelMap[value.toInt()] ?: ""
        }
    )
    val startAxis = VerticalAxis.rememberStart(
        title = "Avg. position",
        titleComponent = TextComponent(
            padding = Insets(8.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Medium)
        )
    )
    val columnColors = listOf(Color(0xFF2196F3), Color(0xFF4CAF50)) // Dry, Rain
    val legendLabelComponent = rememberTextComponent(
        style = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        )
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    columnColors.map { color ->
                        rememberLineComponent(fill = Fill(color), thickness = 16.dp)
                    }
                )
            ),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
            legend =
                rememberHorizontalLegend(
                    items = { extraStore ->
                        extraStore.getOrNull(legendKey)?.forEachIndexed { index, label ->
                            add(
                                LegendItem(
                                    ShapeComponent(
                                        fill = Fill(columnColors[index]),
                                        shape = CorneredShape.Pill,
                                    ),
                                    legendLabelComponent,
                                    label
                                )
                            )
                        }
                    },
                    padding = Insets(top = 16.dp, start = 16.dp),
                ),
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp, top = 8.dp, start = 8.dp, end = 8.dp)
    )
}
