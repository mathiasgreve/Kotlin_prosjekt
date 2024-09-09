package no.uio.ifi.in2000.team32.prosjekt

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.FrostHavvarselDataSource
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.LocationForecastDataSource
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.MetAlertsDataSource
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.OceanForecastDataSource
import no.uio.ifi.in2000.team32.prosjekt.data.repository.LocationForecastRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.MetAlertsRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.OceanForecastRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.SwimmingSpotRepository
import no.uio.ifi.in2000.team32.prosjekt.domain.GetWeatherForecastUseCase
import no.uio.ifi.in2000.team32.prosjekt.networkChecker.NetworkStatusHelper
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.alertscreen.AlertScreen
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.alertscreen.AlertViewModel
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.MapScreen
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.MapViewModel
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.helpscreen.HelpScreen
import no.uio.ifi.in2000.team32.prosjekt.ui.theme.ProsjektTheme
import no.uio.ifi.in2000.team32.prosjekt.userlocation.UserLocationManager


class MainActivity : ComponentActivity() {

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            //Oppretter repository-objekter
            val swimmingSpotRepository = SwimmingSpotRepository(frostHavvarselDataSource = FrostHavvarselDataSource())
            val locationForecastRepository = LocationForecastRepository(locationForecastDataSource = LocationForecastDataSource())
            val metAlertsRepository = MetAlertsRepository(metAlertsDataSource = MetAlertsDataSource())
            val oceanForecastRepository = OceanForecastRepository(oceanForecastDataSource = OceanForecastDataSource())

            //Legger til harware managers
            val networkStatusHelper = NetworkStatusHelper(this.application)
            val userLocationManager = UserLocationManager(this.application)

            //Oppretter useCase-objekter
            val getWeatherForecastUseCase = GetWeatherForecastUseCase(oceanForecastRepository, locationForecastRepository)

            //Oppretter ViewModels
            val mapViewModel = MapViewModel(
                userLocationManager,
                networkStatusHelper,
                metAlertsRepository,
                swimmingSpotRepository,
                getWeatherForecastUseCase
            )
            val alertViewModel = AlertViewModel(metAlertsRepository)

            setContent {
                ProsjektTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "maps") {

                            composable(route = "maps") { MapScreen(navController, mapViewModel) }
                            composable("help") { HelpScreen(navController = navController) }
                            composable(route = "alertScreen/{latitude}/{longitude}") { backStackEntry ->
                                val latitude = backStackEntry.arguments?.getString("latitude")
                                val longitude = backStackEntry.arguments?.getString("longitude")
                                AlertScreen(latitude, longitude, navController, alertViewModel)
                            }
                        }
                    }
                }
            }
        }


    //Main-metode
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MAIN-ACTIVITY", "Program started")
        super.onCreate(savedInstanceState)

        //Sjekker for tilganger til bruk av posisjon
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
}