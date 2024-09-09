package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.ui.components.graphs.TemperatureCharts
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.MapViewModel

@Composable
fun GraphSheet(mapViewModel: MapViewModel, navController: NavController, textColor: Color) {
    // Collect the weather forecast data from the ViewModel
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
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

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

            is Resource.Error -> {
                Text("Error loading data: ${forecastResource.exception.localizedMessage}")
            }
        }

        // ALERT CARD
        when (val alerts = alertsResource.value) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                if (alerts.data.listOfAlerts.isNotEmpty()) {
                    AlertCard(alertList = alerts.data, navController = navController)
                }
            }

            is Resource.Error -> {
                Text("Error loading alert data: ${alerts.exception.localizedMessage}")
            }
        }

        // VÆRMELDING-TABELL OG GRAFER
        when (val forecastResource = weatherForecastResource.value) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                if (forecastResource.data.listOfWeatherForecast.isNotEmpty()) {
                    TemperatureCharts(forecastResource.data.listOfWeatherForecast)
                } else {
                    Text("No weather data available")
                }
            }

            is Resource.Error -> {
                Text("Error loading data: ${forecastResource.exception.localizedMessage}")
            }
        }
    }
}