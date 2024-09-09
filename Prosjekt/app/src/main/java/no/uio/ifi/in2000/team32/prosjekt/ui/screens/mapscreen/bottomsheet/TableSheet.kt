package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.bottomsheet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.getWeatherIconFileName
import no.uio.ifi.in2000.team32.prosjekt.model.weatherforecast.WeatherForecast
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.MapViewModel

@Composable
fun TableSheet(
    mapViewModel: MapViewModel,
    navController: NavController,
    textColor: Color
) {
    val weatherForecastResource = mapViewModel.weatherForecastList.collectAsState()
    val alertsResource = mapViewModel.alerts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(662.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // STEDSNAVN
        when (val forecastResource = weatherForecastResource.value) {
            is Resource.Success -> {
                if (forecastResource.data.locationName != null) {
                    Text(
                        "${forecastResource.data.locationName}",
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            else -> Log.w("BottomSheet", "WeatherForecastResource does not contain data")
        }

        // ALERT CARD
        when (val alerts = alertsResource.value) {
            is Resource.Success -> {
                if (alerts.data.listOfAlerts.isNotEmpty()) {
                    AlertCard(
                        alertList = alerts.data, navController = navController
                    )
                }
            }

            else -> Log.w("BottomSheet", "alertsResource does not contain data")
        }

        // VÆRMELDING-TABELL
        when (val forecastResource = weatherForecastResource.value) {
            is Resource.Loading -> {
                Text(
                    "Loading weather data...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                // TABELL-HEADER
                TableHeader(textColor)

                // TABELL
                val forecastList: List<WeatherForecast> =
                    forecastResource.data.listOfWeatherForecast

                LazyColumn {
                    itemsIndexed(forecastList) { index, forecast ->
                        val iconFileName = getWeatherIconFileName(forecast.weatherSymbol ?: "")
                        val imagePath =
                            "file:///android_asset/weatherForeCastSymbols/200/$iconFileName"
                        val iconPainter = rememberAsyncImagePainter(model = imagePath)

                        val emptyValueStr = "--:--"

                        TableStructure(time = if (index == 0) "nå" else forecast.hour
                            ?: emptyValueStr,
                            icon = iconPainter,
                            data = listOf(
                                forecast.airTemp?.let { "$it°" } ?: emptyValueStr,
                                forecast.rain?.let { "${it}mm" } ?: emptyValueStr,
                                forecast.oceanTemperature?.let { "$it°" } ?: emptyValueStr,
                                forecast.waveHeight?.let { "${it}m" } ?: emptyValueStr,
                                forecast.windSpeed?.let { "${it}m/s" } ?: emptyValueStr,
                            ),
                            textColor = textColor)
                    }
                }
            }

            is Resource.Error -> {
                Text(
                    "Error fetching weather data: ${forecastResource.exception.localizedMessage}",
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun TableHeader(textColor: Color) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text( // There are more elements in header. This helps avoid weird orders
                "  ", modifier = Modifier
                    .weight(1.5f)
                    .wrapContentWidth(Alignment.Start)
            )

            val dataTypes = listOf(
                " Luft- temp. ", " Nedbør ", " Vann- temp. ", " Bølge- høyde ", " Vind- styrke "
            )
            dataTypes.forEach {
                Text(
                    text = it,
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun TableStructure(
    time: String, icon: Painter?, data: List<String>, textColor: Color
) {
    Column {
        val fontWeight: FontWeight?
        val rowModifier: Modifier
        val style: TextStyle

        if (time == "nå") {
            fontWeight = FontWeight.Bold
            rowModifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .fillMaxWidth()
                .padding(16.dp)
            style = MaterialTheme.typography.bodyLarge
        } else {
            fontWeight = null
            rowModifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            style = MaterialTheme.typography.bodyMedium
        }

        Table(
            time,
            icon,
            data,
            textColor,
            fontWeight = fontWeight,
            style = style,
            rowModifier = rowModifier,
            textModifier = Modifier.weight(1f)
        )

        HorizontalDivider()
    }
}

@Composable
fun Table(
    time: String,
    icon: Painter?,
    data: List<String>,
    textColor: Color,
    fontWeight: FontWeight?,
    style: TextStyle,
    rowModifier: Modifier,
    textModifier: Modifier
) {
    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            time,
            color = textColor,
            style = style,
            modifier = Modifier.weight(0.5f),
            fontWeight = fontWeight
        )

        icon?.let {
            Image(
                painter = it,
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(36.dp)
                    .weight(1f)
            )
        }

        data.forEach {
            Text(
                it,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = fontWeight,
                modifier = textModifier
            )
        }
    }
}