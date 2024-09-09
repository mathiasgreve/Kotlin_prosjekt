package no.uio.ifi.in2000.team32.prosjekt.networkChecker

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**

I denne oppgaven har jeg måttet la meg innspirerer av denne youtube videoen

Forfatter/produsent: Philipp Lackner
Tittel: How to Observe Internet Connectivity in Android - Android Studio Tutorial
Link: https://www.youtube.com/watch?v=TzV0oCRDNfM

 **/

//En hjelpeklasse som holder styr på enhetens nettverkstilgang
class NetworkStatusHelper(context: Context) {

    // Instansvariabler
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _networkStatus = MutableStateFlow(isInitiallyConnected())
    val networkStatus: StateFlow<Boolean> = _networkStatus.asStateFlow()


    // Metode for å gi feilmelding ved nettverksbrudd
    fun getNetworkErrorMessage(): String {
        return "Internettforbindelse er utilgjengelig \uD83D\uDE22 \n"      //Beskjed med smiley
    }

    // Objekt som holder styr på netverkStatus
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        //True hvis enheten har internett
        override fun onAvailable(network: Network) {
            _networkStatus.value = true
        }

        //False hvis enheten ikke har internett
        override fun onLost(network: Network) {
            _networkStatus.value = false
        }
    }

    //Initialiserer connectivityManager
    init {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    // En metode som sjekker ved oppstart om nettverket er tilgjengelig
    fun isInitiallyConnected(): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)
        return (connection != null) && connection.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}


