package no.uio.ifi.in2000.team32.prosjekt.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.OceanForecastDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.Details
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.Geometry
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.Instant
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.OceanForecast
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.Properties
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.SeaWaterData
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.SeaWaterMeta
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.TimeSeries
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

class OceanForecastRepositoryTest {

    private lateinit var mockDataSource: OceanForecastDataSource
    private lateinit var repository: OceanForecastRepository

    @BeforeEach
    fun setup() {
        mockDataSource = mockk()
        repository = OceanForecastRepository(mockDataSource)
    }

    @Test
    fun `getForecastForTime returns correct forecast data for given hour`() = runTest {
        // Arrange
        val lat = "60.0"
        val lon = "5.0"
        val hoursAhead = 2
        val details = Details(
            sea_water_to_direction = 300.0,
            sea_surface_wave_height = 2.0,
            sea_water_speed = 3.0,
            sea_water_temperature = 15.0,
            sea_surface_wave_from_direction = 280.0
        )
        val forecastData = createMockForecastData(details)

        coEvery { mockDataSource.fetchOceanForecast(lat, lon) } returns forecastData

        // Act
        val result = repository.getForecastForTime(lat, lon, hoursAhead)

        // Assert
        assertEquals(15.0, result.oceanTemperature)
        assertEquals(3.0, result.waterSpeed)
        assertEquals(2.0, result.waveHeight)
    }

    @Test
    fun `getForecastForTime handles multiple requests and utilizes cache correctly`() = runTest {
        // Arrange
        val lat = "60.0"
        val lon = "5.0"
        val hoursAhead = 2

        // Mock response for the first request
        val firstDetails = Details(
            sea_surface_wave_from_direction = 300.0,
            sea_surface_wave_height = 2.0,
            sea_water_speed = 3.0,
            sea_water_temperature = 15.0,
            sea_water_to_direction = 280.0
        )
        val firstForecastData = createMockForecastData(firstDetails)
        coEvery { mockDataSource.fetchOceanForecast(lat, lon) } returns firstForecastData

        // Make the first request to initialize cache
        repository.getForecastForTime(lat, lon, hoursAhead)

        // Mock response for the second request with different details
        val secondDetails = Details(
            sea_surface_wave_from_direction = 310.0,
            sea_surface_wave_height = 2.5,
            sea_water_speed = 3.5,
            sea_water_temperature = 16.0,
            sea_water_to_direction = 290.0
        )
        val secondForecastData = createMockForecastData(secondDetails)

        // Setup the second call to return different data if called again
        coEvery { mockDataSource.fetchOceanForecast(lat, lon) } returns secondForecastData

        // Act - Second request should use cached data
        val result = repository.getForecastForTime(lat, lon, hoursAhead)

        // Assert - Ensure cached data is used, not the new mock
        assertEquals(15.0, result.oceanTemperature)
        assertEquals(3.0, result.waterSpeed)
        assertEquals(2.0, result.waveHeight)
        coVerify(exactly = 1) {
            mockDataSource.fetchOceanForecast(
                lat,
                lon
            )
        } // Verifying that the fetch function was called only once
    }

    @Test
    fun `getForecastForTime handles API failures gracefully`() = runTest {
        // Arrange
        val lat = "60.0"
        val lon = "5.0"
        val hoursAhead = 2
        coEvery {
            mockDataSource.fetchOceanForecast(
                any(),
                any()
            )
        } throws RuntimeException("Network error")

        // Act
        val result = kotlin.runCatching { repository.getForecastForTime(lat, lon, hoursAhead) }

        assertDoesNotThrow("Should catch when data source throws exception") { result.getOrThrow() }
        assertNull(result.getOrThrow().oceanTemperature, "Ocean temp should be null on API failure")
        assertNull(result.getOrThrow().waterSpeed, "Water speed should be null on API failure")
        assertNull(result.getOrThrow().waveHeight, "Wave height should be null on API failure")
    }

    /**
     * Creates mock data for OceanForecast to be used in tests.
     *
     * This function generates an `OceanForecast` object populated with hardcoded forecast data
     * for 24 hours, each with the same provided `details`.
     *
     * @param details The `Details` object containing the specific forecast information to be used
     *                for each hour in the timeseries.
     * @return An `OceanForecast` object containing mock ocean forecast data.
     */
    private fun createMockForecastData(details: Details): OceanForecast {
        val timeseries = List(24) { index ->
            TimeSeries(
                time = "2024-05-08T${index}:00:00Z",
                data = SeaWaterData(
                    instant = Instant(
                        details = details
                    )
                )
            )
        }

        return OceanForecast(
            type = "Feature",
            geometry = Geometry(
                type = "Point",
                coordinates = listOf(5.0, 60.0)
            ),
            properties = Properties(
                meta = SeaWaterMeta(
                    updated_at = "2024-05-07T19:37:36Z",
                    units = mapOf(
                        "sea_surface_wave_from_direction" to "degrees",
                        "sea_surface_wave_height" to "m",
                        "sea_water_speed" to "m/s",
                        "sea_water_temperature" to "celsius",
                        "sea_water_to_direction" to "degrees"
                    )
                ),
                timeseries = timeseries
            )
        )
    }
}
