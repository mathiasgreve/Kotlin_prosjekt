package no.uio.ifi.in2000.team32.prosjekt.data.repository


import android.util.Log
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.LocationForecastDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Data
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Feature


/**
 * Represents the data collected from a query to the LocationForecast API
 */
data class ProcessedLocationForecast( // Consider changing to non-nullable in the future.
    // Will require some work to ensure null-safety
    val airTemperature: Double?,
    val windSpeed: Double?,
    val windFromDirection: Double?,
    val ultravioletIndex: Double?,
    val weatherSymbol: String?,
    val rain: Double?,

    val hour: String?
)

class LocationForecastRepository(
    private var locationForecastDataSource: LocationForecastDataSource
) {

    //cache
    private var prevLat: String? = null
    private var prevLon: String? = null
    private lateinit var response: Feature


    /**
     * Collects and returns the forecast for a given time at a given location
     * @param lat: The latitude to search
     * @param lon: The longitude to search
     * @param hoursAhead: An int representing how many hours in the future you want to search. If
     * for example want the result for 2 hours in the future, hoursAhead is 2
     * @return An object of data class ProcessedLocationForecast,
     * containing the relevant data collected from the API
     */
    suspend fun getForecastForTime(
        lat: String,
        lon: String,
        hoursAhead: Int
    ): ProcessedLocationForecast {

        val forecastData: Data? = getForecastData(lat, lon, hoursAhead)
        val forecast: Map<String, Double>? = forecastData?.instant?.details

        return ProcessedLocationForecast(
            airTemperature = forecast?.get("air_temperature"),
            windSpeed = forecast?.get("wind_speed"),
            windFromDirection = forecast?.get("wind_from_direction"),
            ultravioletIndex = forecast?.get("ultraviolet_index_clear_sky"),
            weatherSymbol = forecastData?.next_1_hours?.summary?.symbol_code, // We decide which specific time to get symbol for
            rain = forecastData?.next_1_hours?.details?.get("precipitation_amount"),
            hour = forecastData?.let { getHourOfDay(hoursAhead) }
        )
    }

    /**
     * Helper method for every get method in LocationForecastRepository
     * @param lat: The latitude to search
     * @param lon: The longitude to search
     * @param hoursAhead: An int representing how many hours in the future you want to search. If
     * for example want the result for 2 hours in the future, hoursAhead is 2
     * @return An object of data class Data. Contains info for the forecast of a specific timeseries
     */
    private suspend fun getForecastData(lat: String, lon: String, hoursAhead: Int): Data? {
        if ((prevLat != lat || prevLon != lon)) {
            prevLat = lat
            prevLon = lon

            try {
                response = locationForecastDataSource.fetchLocationForecast(
                    latitude = lat,
                    longitude = lon
                )
            } catch (e: RuntimeException) { // API-failure, eller manglende nettverk
                return null
            }
        }

        return try {
            response.properties.timeseries[hoursAhead].data
        } catch (e: IndexOutOfBoundsException) {
            Log.w("getForecastData", "No location forecasts at given location")
            null
        } catch (e: UninitializedPropertyAccessException) {
            null
        }
    }

    /**
     * Gets the time of the cached forecast.
     * Assumes response cache is already initialized and up to date. Calling this method uninitialized
     * will crash
     * @param hoursAhead: An int representing how many hours in the future you want to search. If you
     * for example want the result for 2 hours in the future, hoursAhead is 2
     * @return String representing the hour (24-hour format) found using hoursAhead on cached response
     * @throws UninitializedPropertyAccessException if a response is not yet cached
     */
    private fun getHourOfDay(hoursAhead: Int): String {
        val time = response.properties.timeseries[hoursAhead + 2].time // We do 'hoursAhead + 2' because Norway's time is two hours ahead of the API

        return "${time[11]}${time[12]}"
    }
}