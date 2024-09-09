package no.uio.ifi.in2000.team32.prosjekt.domain

import no.uio.ifi.in2000.team32.prosjekt.data.repository.LocationForecastRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.OceanForecastRepository
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.model.weatherforecast.WeatherForecast
import no.uio.ifi.in2000.team32.prosjekt.model.weatherforecast.WeatherForecastList


/**
 * Class representing the use case of needing both data from OceanForecast API and LocationForecast API
 */
class GetWeatherForecastUseCase(
    private val oceanForecastRepository: OceanForecastRepository,
    private val locationForecastRepository: LocationForecastRepository
) {

    /**
     * When invoked, collects and returns the data from
     * both Location Forecast API and Ocean Forecast API
     * @param spot: Spot object containing longitude and latitude
     * @return A WeatherForecastList containing weather forecasts for the next 24 hours
     */
    suspend operator fun invoke(spot: Spot): WeatherForecastList {
        val weatherForecasts = WeatherForecastList(spot.swimmingSpotName, mutableListOf())

        repeat(24) {
            weatherForecasts.listOfWeatherForecast.add(fetchWeatherData(spot, it))
        }
        return weatherForecasts
    }

    /**
     * Fetches weather data for a specific spot and time.
     * It combines data from both ocean and location forecasts and returns a [WeatherForecast] object.
     * @param spot A [Spot] object representing the location (latitude and longitude).
     * @param hoursAhead An integer representing how many hours ahead the forecast should be retrieved for. Default is 0.
     * @return A [WeatherForecast] object containing data such as temperature, wind speed, wave height, etc.
     */
    private suspend fun fetchWeatherData(spot: Spot, hoursAhead: Int = 0): WeatherForecast {
        val lat = spot.lat
        val lon = spot.lon
        val oceanForecast = oceanForecastRepository.getForecastForTime(lat, lon, hoursAhead)
        val locationForecast = locationForecastRepository.getForecastForTime(lat, lon, hoursAhead)

        return WeatherForecast(
            oceanTemperature = oceanForecast.oceanTemperature,
            waterSpeed = oceanForecast.waterSpeed,
            waveHeight = oceanForecast.waveHeight,

            airTemp = locationForecast.airTemperature,
            windSpeed = locationForecast.windSpeed,
            windDirection = locationForecast.windFromDirection,
            strengthOfUV = locationForecast.ultravioletIndex,
            weatherSymbol = locationForecast.weatherSymbol,
            rain = locationForecast.rain,
            hour = locationForecast.hour
        )
    }
}




