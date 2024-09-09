package no.uio.ifi.in2000.team32.prosjekt.model.weatherforecast

data class WeatherForecast(
    val oceanTemperature: Double?,
    val waterSpeed: Double?,
    val waveHeight: Double?,

    val airTemp: Double?,
    val windSpeed: Double?,
    val windDirection: Double?,
    val strengthOfUV: Double?,
    val weatherSymbol: String?,
    val rain: Double?,

    val hour: String?
)

data class WeatherForecastList(
    val locationName: String?,
    val listOfWeatherForecast: MutableList<WeatherForecast>
)