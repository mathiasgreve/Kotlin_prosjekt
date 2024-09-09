package no.uio.ifi.in2000.team32.prosjekt.ui.screens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.AlertList
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot

interface AlertUpdater {
    @Suppress("PropertyName") // _alerts is normal naming for view model, IDE doesn't kbow this inteface is for ViewModels
    val _alerts: MutableStateFlow<Resource<AlertList>>
    val alerts: StateFlow<Resource<AlertList>>

    /**
     * Updates alerts variable
     * @param spot: A spot object representing where you want to look for alerts
     */
    fun updateAlerts(spot: Spot)
    // Hvis mulig hadde vi likt å bruke inheritance for å ikke ha denne abstrakt,
    // ettersom koden er lik i begge implementasjoner. Dette ble derimot
    // litt vanskelig pga MapViewModel arver fra UserLocationViewModel, og AlertViewModel ikke
    // gjør det. På tidspress var dette beste løsningen, men er likevel delvis et brudd på
    // "don't repeat yourself"
}