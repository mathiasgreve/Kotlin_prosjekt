package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team32.prosjekt.data.repository.MapboxRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.MetAlertsRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.SwimmingSpotRepository
import no.uio.ifi.in2000.team32.prosjekt.domain.GetWeatherForecastUseCase
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.AlertList
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.model.weatherforecast.WeatherForecastList
import no.uio.ifi.in2000.team32.prosjekt.networkChecker.NetworkStatusHelper
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.AlertUpdater
import no.uio.ifi.in2000.team32.prosjekt.userlocation.UserLocationManager


class MapViewModel(
    userLocationManager: UserLocationManager,
    networkStatusHelper: NetworkStatusHelper,
    private val metAlertsRepository: MetAlertsRepository,
    private val swimmingSpotRepository: SwimmingSpotRepository,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase
) : UserLocationViewModel(userLocationManager, networkStatusHelper), AlertUpdater {

    //Repo for søkefelt i mapbox
    private val mapBoxRepository = MapboxRepository()

    // ----STATE VARIABLER FRA DATALAGET---- // Disse burde kanskje brukes som UI-states?
    private val _relevantSwimmingSpots = MutableStateFlow<Resource<List<Spot>>>(Resource.Loading())
    val relevantSwimmingSpots = _relevantSwimmingSpots.asStateFlow()

    private val _weatherForecastList =
        MutableStateFlow<Resource<WeatherForecastList>>(Resource.Loading())
    val weatherForecastList = _weatherForecastList.asStateFlow()

    private val _searchSuggestions =
        MutableStateFlow<List<String>>(emptyList()) // bottomBar - Listen over relevante stedsnavn som popper opp mens brukeren skriver i søkefeltet
    val searchSuggestions = _searchSuggestions.asStateFlow()

    override val _alerts = MutableStateFlow<Resource<AlertList>>(Resource.Loading())
    override val alerts = _alerts.asStateFlow()


    //Karttilstand
    @OptIn(MapboxExperimental::class)
    val mapViewportState = MapViewportState()


    fun fetchSuggestions(query: String) {
        viewModelScope.launch {
            if (isNetworkAvailable.value) {
                val results = mapBoxRepository.getSuggestions(query, country = "NO")
                _searchSuggestions.value = results
            } else {
                _searchSuggestions.value = listOf(errorMessage.value)
            }

        }
    }

    fun searchCity(query: String) {
        viewModelScope.launch {
            val point = mapBoxRepository.getCityLocation(query, country = "NO")
            point?.let {
                moveMapToLocation(point.latitude(), point.longitude())
            }
                ?: println("Ingen gyldige resultater funnet for: $query")       //Skricer ut feilmelding
        }
    }


    fun updateRelevantSwimmingSpots(spot: Spot) {
        viewModelScope.launch {
            try {
                _relevantSwimmingSpots.value =
                    Resource.Loading()

                val listOfSwimmingSpots = swimmingSpotRepository.getSwimmingSpots(
                    spot.lat, spot.lon, "5"
                )

                _relevantSwimmingSpots.value =
                    Resource.Success(listOfSwimmingSpots)  // oppdater tilstand til success med de hentede dataene

            } catch (e: Exception) {
                _relevantSwimmingSpots.value = Resource.Error(e)
                // Her kan man også logge feilen eller informerer bruker
            }
        }
    }


    override fun updateAlerts(spot: Spot) {
        viewModelScope.launch {
            try {
                _alerts.value = Resource.Loading() //sett tilstand til loading før asynkront kall
                val alertList = metAlertsRepository.findAlerts(
                    spot.lat.toDouble(), spot.lon.toDouble()
                ) // gjør asynkront kall og hent data
                _alerts.value =
                    Resource.Success(alertList)  // oppdater tilstand til success med de hentede dataene
            } catch (e: Exception) {
                _alerts.value =
                    Resource.Error(e)  // Her kan man også logge feilen eller informerer brukeren
            }
        }
    }

    fun updateWeatherForecast(weatherForecastLocation: Spot) {
        viewModelScope.launch {
            try {
                _weatherForecastList.value =
                    Resource.Loading() //sett tilstand til loading før asynkront kall

                val forecast =
                    getWeatherForecastUseCase(weatherForecastLocation) // gjør asynkront kall og hent data

                _weatherForecastList.value =
                    Resource.Success(forecast)  // oppdater tilstand til success med de hentede dataene
            } catch (e: Exception) {
                _weatherForecastList.value =
                    Resource.Error(e)  // Her kan man også logge feilen eller informerer brukeren
            }
        }
    }


    //Meknaikk for å flytte kamera til kartsøk
    @OptIn(MapboxExperimental::class)
    fun moveMapToLocation(lat: Double, lon: Double) {
        viewModelScope.launch {

            // Oppdaterer kartets visningspunkt uten å endre annen tilstand
            mapViewportState.setCameraOptions(
                CameraOptions.Builder().center(Point.fromLngLat(lon, lat))
                    .zoom(12.0)  // Juster zoom-nivå etter behov
                    .build()
            )
            val spot = Spot(lat.toString(), lon.toString())
            updateRelevantSwimmingSpots(spot)
            updateAlerts(spot)
        }
    }

    /**
     * Moves the center of the map to the user's location (if location is found)
     */
    @OptIn(MapboxExperimental::class)
    fun moveToUserLocation() {
        viewModelScope.launch {
            when (val locationResource = userLocation.value) {
                is Resource.Success -> {
                    val point = locationResource.data
                    val lat = point.latitude()
                    val lon = point.longitude()
                    mapViewportState.flyTo(
                        cameraOptions = CameraOptions.Builder().center(Point.fromLngLat(lon, lat))
                            .zoom(12.0)  // Juster zoom-nivå etter behov
                            .build()
                    )
                    // Oppdaterer API for å hente nye badeplasser basert på brukerens nåværende lokasjon
                    updateRelevantSwimmingSpots(Spot(lat.toString(), lon.toString()))
                    updateAlerts(Spot(lat.toString(), lon.toString()))
                }

                else -> Log.w(
                    "moveToUserLocation",
                    "moveToUserLocation was called, while no user location is found"
                )
            }
        }
    }
}


