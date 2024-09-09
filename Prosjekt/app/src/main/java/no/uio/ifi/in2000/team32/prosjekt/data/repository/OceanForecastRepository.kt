package no.uio.ifi.in2000.team32.prosjekt.data.repository

import android.util.Log
import io.ktor.client.call.NoTransformationFoundException
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.OceanForecastDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.Details
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.OceanForecast

data class ProcessedOceanForecast(
    val oceanTemperature: Double?,
    val waterSpeed: Double?,
    val waveHeight: Double?
)

/**
 * Repository for getting forecasts about temperature, sea water speed and wave height, at
 * different timestamps
 */
class OceanForecastRepository(private val oceanForecastDataSource: OceanForecastDataSource) {

    // cache
    private var prevLat: String? = null
    private var prevLon: String? = null
    private lateinit var response: OceanForecast

    /**
     * Method for collecting the specific ocean temperature for a given location at a given time
     * @param lat: The latitude to search
     * @param lon: The longitude to search
     * @param hoursAhead: An int representing how many hours in the future you want to search. If
     * for example want the result for 2 hours in the future, hoursAhead is 2
     * @return A ProcessedOceanForecast object,
     * that contains oceanTemperature, water speed, and height of waves
     */
    suspend fun getForecastForTime(
        lat: String,
        lon: String,
        hoursAhead: Int
    ): ProcessedOceanForecast {
        val data: Details? = getDetailsOfForecast(lat, lon, hoursAhead)

        return ProcessedOceanForecast(
            oceanTemperature = data?.sea_water_temperature,
            waterSpeed = data?.sea_water_speed,
            waveHeight = data?.sea_surface_wave_height
        )
    }

    /**
     * Helper method to get the details of a query to Location Forecast API
     * @param lat: The latitude to search
     * @param lon: The longitude to search
     * @param hoursAhead: An int representing how many hours in the future you want to search. If
     * for example want the result for 2 hours in the future, hoursAhead is 2
     * @return An object of class Details. Containing all the info of the given forecast
     */
    private suspend fun getDetailsOfForecast(lat: String, lon: String, hoursAhead: Int): Details? {
        if ((prevLat != lat || prevLon != lon) || !::response.isInitialized) {
            prevLat = lat
            prevLon = lon

            try {
                response = oceanForecastDataSource.fetchOceanForecast(lat = lat, lon = lon)
            } catch (e: NoTransformationFoundException) {
                Log.w("getDetailsOfForecast", "Query is outside the scope of OceanForecast API")
                return null
            } catch (e: RuntimeException) { // API-failure, eller mangel på internett-tilgang
                return null
            }
        }

        return try {
            response.properties.timeseries[hoursAhead].data.instant.details
        } catch (e: IndexOutOfBoundsException) {
            Log.w("getDetailsOfForecast", "No ocean forecasts at given location")
            return null
        }
    }
}