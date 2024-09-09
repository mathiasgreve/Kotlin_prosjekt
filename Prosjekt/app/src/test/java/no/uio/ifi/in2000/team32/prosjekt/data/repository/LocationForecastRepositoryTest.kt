package no.uio.ifi.in2000.team32.prosjekt.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.LocationForecastDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Data
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Feature
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.ForecastPeriod
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Geometry
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.InstantDetails
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Meta
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Properties
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Summary
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.TimeSeries
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

/** For unit tests it is useful to have consistent and controlled input data. Sources for unit testing with JUnit5 and MockK:
 *
 * https://developer.android.com/training/testing/local-tests
 * https://junit.org/junit5/ and https://github.com/mannodermaus/android-junit5
 * https://mockk.io/ANDROID.html, the library used to create controlled input data by generating mock objects.
 */

class LocationForecastRepositoryTest {

    private lateinit var mockDataSource: LocationForecastDataSource
    private lateinit var repository: LocationForecastRepository

    @BeforeEach
    fun setUp() {
        mockDataSource = mockk()
        repository = LocationForecastRepository(mockDataSource)
    }

    @Test
    fun `test getForecastForTime should return correct forecast`() = runTest {  // Positive test
        val mockFeature = getMockLocationForecast()
        // Simuler at API-kallet returnerer en prognose
        coEvery { mockDataSource.fetchLocationForecast(any(), any()) } returns mockFeature

        // Forespør prognose for "nåværende" tid
        val forecast = repository.getForecastForTime("58.01", "7.05", 0)

        // Assert
        assertEquals(6.2, forecast.airTemperature)
        assertEquals(2.1, forecast.windSpeed)
        assertEquals(37.1, forecast.windFromDirection)
        assertEquals(0.5, forecast.ultravioletIndex)
        assertEquals("cloudy", forecast.weatherSymbol)
    }

    @Test
    fun `test getForecastForTime handles API failure gracefully`() = runTest {
        // Simuler en API-svikt ved å kaste en RuntimeException
        coEvery {
            mockDataSource.fetchLocationForecast(
                any(),
                any()
            )
        } throws RuntimeException("API failure")

        // Forsøk å hente værprognosen
        val result = runCatching { repository.getForecastForTime("58.01", "7.05", 0) }

        assertDoesNotThrow("Should catch during API failure") { result.getOrThrow() }

        // Sjekk at alle verdier er null eller standard, avhengig av hva vi returnerer i feilhåndteringen
        val forecast = result.getOrThrow()
        assertNull(forecast.airTemperature, "Expected null for air temperature on API failure")
        assertNull(forecast.windSpeed, "Expected null for wind speed on API failure")
        assertNull(
            forecast.windFromDirection,
            "Expected null for wind from direction on API failure"
        )
        assertNull(forecast.ultravioletIndex, "Expected null for UV index on API failure")
        assertNull(forecast.weatherSymbol, "Expected null for weather symbol on API failure")
        assertNull(forecast.rain, "Expected null for rain on API failure")
        assertNull(forecast.hour, "Expected null for hour on API failure")
    }

    /*
    'getMockLocationForecast' genererer kontrollerte testdata, og 'createData' er ment å tilby fleksibel
    opprettelse av ulike værscenarier.

    Testoppsettet simulerer oppførsel for å reflektere virkeligheten
    i APIet og sikrer at koden håndterer tidsseriedataene korrekt.
     */
    private fun getMockLocationForecast(): Feature {
        val timeseries = listOf(
            createData("2024-05-08T16:00:00Z", 6.2, 2.1, "cloudy", 51.9, 0.0, 11.0, 37.1, 0.5),
            createData("2024-05-08T17:00:00Z", 7.4, 3.5, "sunny", 10.0, 0.2, 20.0, 40.0, 1.0),
            createData("2024-05-08T18:00:00Z", 70.4, 30.5, "sunny", 10.0, 1.0, 20.0, 100.0, 2.0)
        )

        val properties = Properties(
            meta = Meta(
                updated_at = "2024-05-08T15:40:34Z",
                units = mapOf(
                    "air_pressure_at_sea_level" to "hPa",
                    "air_temperature" to "celsius",
                    "wind_speed" to "m/s",
                    "wind_from_direction" to "degrees",
                    "relative_humidity" to "%",
                    "ultraviolet_index_clear_sky" to "index"
                )
            ),
            timeseries = timeseries
        )

        return Feature(
            type = "Feature",
            geometry = Geometry(type = "Point", coordinates = listOf(58.01, 7.05)),
            properties = properties
        )
    }

    private fun createData(
        time: String,
        temp: Double,
        wind: Double,
        symbol: String,
        probPrecip12h: Double,
        precipAmount1h: Double,
        probPrecip6h: Double,
        windDirection: Double,
        uvIndex: Double
    ): TimeSeries {
        return TimeSeries(
            time = time,
            data = Data(
                instant = InstantDetails(
                    details = mapOf(
                        "air_temperature" to temp,
                        "wind_speed" to wind,
                        "wind_from_direction" to windDirection,
                        "ultraviolet_index_clear_sky" to uvIndex
                    )
                ),
                next_12_hours = ForecastPeriod(
                    summary = Summary(symbol_code = symbol),
                    details = mapOf(
                        "probability_of_precipitation" to probPrecip12h
                    )
                ),
                next_1_hours = ForecastPeriod(
                    summary = Summary(symbol_code = symbol),
                    details = mapOf(
                        "precipitation_amount" to precipAmount1h,
                        "probability_of_precipitation" to probPrecip6h
                    )
                ),
                next_6_hours = ForecastPeriod(
                    summary = Summary(symbol_code = symbol),
                    details = mapOf(
                        "air_temperature_max" to temp + 1,
                        "air_temperature_min" to temp - 1,
                        "precipitation_amount" to precipAmount1h,
                        "probability_of_precipitation" to probPrecip6h
                    )
                )
            )
        )
    }
}

