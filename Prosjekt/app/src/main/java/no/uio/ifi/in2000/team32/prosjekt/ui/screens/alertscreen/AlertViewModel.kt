package no.uio.ifi.in2000.team32.prosjekt.ui.screens.alertscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team32.prosjekt.data.repository.MetAlertsRepository
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.AlertList
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.AlertUpdater

class AlertViewModel (
    private val metAlertsRepository: MetAlertsRepository
): ViewModel(), AlertUpdater {

    override val _alerts = MutableStateFlow<Resource<AlertList>>(Resource.Loading())
    override val alerts = _alerts.asStateFlow()

    override fun updateAlerts(spot: Spot) {
        viewModelScope.launch {
            try {
                _alerts.value = Resource.Loading() //sett tilstand til loading før asynkront kall

                val alertList = metAlertsRepository.findAlerts(
                    spot.lat.toDouble(),
                    spot.lon.toDouble()
                ) // gjør asynkront kall og hent data

                _alerts.value =
                    Resource.Success(alertList)  // oppdater tilstand til success med de hentede dataene

            } catch (e: Exception) {
                _alerts.value =
                    Resource.Error(e)  // Her kan man også logge feilen eller informerer brukeren
            }
        }
    }
}

