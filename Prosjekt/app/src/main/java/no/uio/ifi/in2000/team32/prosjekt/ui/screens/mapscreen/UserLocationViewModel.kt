package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.networkChecker.NetworkStatusHelper
import no.uio.ifi.in2000.team32.prosjekt.userlocation.UserLocationManager


abstract class UserLocationViewModel(
    private val userLocationManager: UserLocationManager,
    private val networkStatusHelper: NetworkStatusHelper
): ViewModel() {

    // Tilstand for feilmeldinger, initialiseres som null for å indikere at det ikke er noen feil ved start.
    private val _errorMessage = MutableStateFlow(networkStatusHelper.getNetworkErrorMessage()) // Kanskje denne ikke burde være state? Endrer aldri verdi
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    // Tilstand for å observere nettverkstilgjengelighet fra networkStatusHelper.
    private val _isNetworkAvailable = MutableStateFlow(networkStatusHelper.isInitiallyConnected())
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()

    private val _userLocation = MutableStateFlow<Resource<Point>>(Resource.Loading())
    val userLocation: StateFlow<Resource<Point>> = _userLocation.asStateFlow()

    init {
        if (userLocationManager.startLocationUpdates()) {
            observeLocationUpdates()
        }
        observeNetworkStatus()
    }

    // antar at denne funksjonen oppdatere brukerens posisjon i sanntid mens appen brukes
    private fun observeLocationUpdates() {
        viewModelScope.launch {
            try {
                _userLocation.value = Resource.Loading()
                //userLocationRepository.locationUpdates.asFlow().collect() { locationPair ->
                userLocationManager.locationUpdates.asFlow().collect { locationPair ->
                    _userLocation.value = Resource.Success(Point.fromLngLat(locationPair.second, locationPair.first))
                }
            }

            catch (e: Exception){
                _userLocation.value = Resource.Error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userLocationManager.stopLocationUpdates()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatusHelper.networkStatus.collect { status ->
                _isNetworkAvailable.value = status
                if (!status) {
                    _errorMessage.value = networkStatusHelper.getNetworkErrorMessage()
                }
            }
        }
    }
}