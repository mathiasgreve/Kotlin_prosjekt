package no.uio.ifi.in2000.team32.prosjekt.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team32.prosjekt.data.repository.LocationForecastRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.OceanForecastRepository
import no.uio.ifi.in2000.team32.prosjekt.data.repository.ProcessedLocationForecast
import no.uio.ifi.in2000.team32.prosjekt.data.repository.ProcessedOceanForecast
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** For unit tests it is useful to have consistent and controlled input data. Sources for unit testing with JUnit5 and MockK:
 *
 * https://developer.android.com/training/testing/local-tests
 * https://junit.org/junit5/ and https://github.com/mannodermaus/android-junit5
 * https://mockk.io/ANDROID.html, the library used to create controlled input data by generating mock objects.
 */

class GetWeatherForecastUseCaseTest {

    private lateinit var oceanForecastRepository: OceanForecastRepository
    private lateinit var locationForecastRepository: LocationForecastRepository
    private lateinit var getWeatherForecastUseCase: GetWeatherForecastUseCase

    @BeforeEach
    fun setUp() {
        oceanForecastRepository = mockk()
        locationForecastRepository = mockk()
        getWeatherForecastUseCase =
            GetWeatherForecastUseCase(oceanForecastRepository, locationForecastRepository)
    }

    @Test
    fun `invoke should return combined weather forecast for given coordinates and time`() =
        runTest {
            val spot = Spot(lat = "58.01", lon = "7.05", swimmingSpotName = "Lovely Beach")

            // Arrange: Setup mock responses for each repository
            coEvery {
                oceanForecastRepository.getForecastForTime(
                    any(),
                    any(),
                    any()
                )
            } returns ProcessedOceanForecast(10.0, 1.2, 0.5)
            coEvery {
                locationForecastRepository.getForecastForTime(
                    any(),
                    any(),
                    any()
                )
            } returns ProcessedLocationForecast(
                15.0,
                3.0,
                270.0,
                5.0,
                "partly_cloudy",
                0.0,
                "12:00"
            )

            // Act: Invoke the function which is to be tested
            val forecastList = getWeatherForecastUseCase.invoke(spot)

            // Assert: Check the structure and content of the result
            assertEquals("Lovely Beach", forecastList.locationName)
            assertEquals(
                24,
                forecastList.listOfWeatherForecast.size,
                "Should contain 24 hourly forecasts"
            )
            forecastList.listOfWeatherForecast.forEach {
                assertEquals(10.0, it.oceanTemperature)
                assertEquals(1.2, it.waterSpeed)
                assertEquals(0.5, it.waveHeight)
                assertEquals(15.0, it.airTemp)
                assertEquals(3.0, it.windSpeed)
                assertEquals(270.0, it.windDirection)
                assertEquals(5.0, it.strengthOfUV)
                assertEquals("partly_cloudy", it.weatherSymbol)
                assertEquals(0.0, it.rain)
                assertEquals("12:00", it.hour)
            }
        }
}
