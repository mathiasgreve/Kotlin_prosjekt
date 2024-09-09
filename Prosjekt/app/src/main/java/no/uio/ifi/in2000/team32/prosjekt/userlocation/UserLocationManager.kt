package no.uio.ifi.in2000.team32.prosjekt.userlocation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.FusedLocationProviderClient

/**
 * Manages user location updates using the FusedLocationProviderClient.
 *
 * @property context The application context used to initialize the location client and check permissions.
 */
class UserLocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val _locationUpdates = MutableLiveData<Pair<Double, Double>>()
    val locationUpdates: LiveData<Pair<Double, Double>> = _locationUpdates

    /**
     * Starts location updates if the necessary permissions are granted.
     *
     * @return True if location updates were started successfully, false if permissions are missing.
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(): Boolean {
        if (!context.hasLocationPermission()) {
            Log.e("LocationManager", "Mangler nødvendige lokasjonstillatelser.")
            return false
        }

        val locationRequest = LocationRequest.Builder(10_000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        return true
    }

    /**
     * Stops location updates.
     */
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Callback to handle location results and update the LiveData.
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.lastOrNull()?.let { location ->
                _locationUpdates.postValue(Pair(location.latitude, location.longitude))
            }
        }
    }
}

