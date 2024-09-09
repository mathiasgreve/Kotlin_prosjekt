package no.uio.ifi.in2000.team32.prosjekt.ui.components.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import no.uio.ifi.in2000.team32.prosjekt.model.weatherforecast.WeatherForecast
import java.util.Locale

@Composable
fun TemperatureCharts(forecastList: List<WeatherForecast>) {
    val blue = Color(0xFF002EB4).toArgb()
    val lightBlue = Color(0xFF0F97E7).toArgb()
    val extraLightBlue = Color(0xFF6FC0F1).toArgb()

    val airChartColor = if (isSystemInDarkTheme()) extraLightBlue else blue

    val textColor = if (isSystemInDarkTheme()) Color.White.toArgb() else Color.Black.toArgb()

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth(), factory = { context ->
            LineChart(context).apply {

                val airTempEntries = forecastList.mapIndexed { index, forecast ->
                    Entry(
                        index.toFloat(),
                        String.format(Locale.US, "%.1f", forecast.airTemp).toFloatOrNull() ?: 0f
                    )
                }

                val airTemp = LineDataSet(airTempEntries, "Lufttemperatur").apply {
                    color = airChartColor
                    valueTextColor = textColor
                    legend.textColor = textColor

                    setDrawCircles(true)
                    setCircleColor(airChartColor)

                    lineWidth = 4f
                }

                data = LineData(airTemp)

                description.text = "" // Setting description text to empty in order to hide it

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = IndexAxisValueFormatter(forecastList.map { it.hour })
                    granularity = 1f
                }
                xAxis.textColor = textColor

                axisLeft.isEnabled = true
                axisLeft.textColor = textColor

                axisRight.isEnabled = false

                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
            }
        })

        AndroidView(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(), factory = { context ->
            BarChart(context).apply {
                val waterTempEntries = forecastList.mapIndexed { index, forecast ->
                    BarEntry(
                        index.toFloat(),
                        String.format(Locale.US, "%.1f", forecast.oceanTemperature).toFloatOrNull()
                            ?: 0f
                    )
                }

                val waterTemp = BarDataSet(waterTempEntries, "Vanntemperatur").apply {
                    color = lightBlue
                    valueTextColor = textColor
                    barShadowColor = lightBlue
                    legend.textColor = textColor

                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry): String {
                            return if (barEntry.x.toInt() % 2 == 0) String.format(
                                Locale.US, "%.1f°C", barEntry.y
                            ) else ""
                        }
                    }
                }

                data = BarData(waterTemp)
                description.text = "" // Again, setting description text to empty to hide it

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = IndexAxisValueFormatter(forecastList.map { it.hour })
                    granularity = 1f
                }
                xAxis.textColor = textColor

                axisRight.isEnabled = false

                axisLeft.isEnabled = true
                axisLeft.textColor = textColor


                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
            }
        })
    }
}
