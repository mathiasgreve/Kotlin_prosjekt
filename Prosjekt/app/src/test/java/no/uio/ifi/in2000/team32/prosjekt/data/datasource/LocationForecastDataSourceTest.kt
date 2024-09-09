package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Disse dataSource-testene er egentlig ikke ekte enhetstester fordi de ikke kjøres i isolasjon.
 * I stedet avhenger de av internettforbindelse og at APIene ikke er nede. Da kan man si at
 * disse testene er nærmere integrasjonstesting enn enhetstesting. Se også kilder øverst i repository-testene.
 */

class LocationForecastDataSourceTest {

    private val dataSource = LocationForecastDataSource()

    @Test
    fun testCheckConnection() = runBlocking {
        // Arrange & Act
        val response = dataSource.checkConnection()

        // Assert
        assertEquals(200, response.status.value, "Statuskoden bør være 200 OK")
    }

    @Test
    fun testFetchLocationForecastValidData() = runBlocking {
        // Arrange
        val lat = "60.10"
        val lon = "5.00" //Utenfor Bergen

        // Act
        val result = dataSource.fetchLocationForecast(lat, lon)

        // Assert
        assertTrue(result.properties.timeseries.isNotEmpty(), "Timeseries skal inneholde data")
        result.properties.timeseries.forEach {
            assertNotNull(
                it.data.instant.details["air_temperature"],
                "Lufttemperaturen bør ikke være null"
            )
        }
    }
}
