package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.call.NoTransformationFoundException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Disse dataSource-testene er egentlig ikke ekte enhetstester fordi de ikke kjøres i isolasjon.
 * I stedet avhenger de av internettforbindelse og at APIene ikke er nede. Da kan man si at
 * disse testene er nærmere integrasjonstesting enn enhetstesting. Se også kilder øverst i repository-testene.
 */

class OceanForecastDataSourceTest {

    private val dataSource = OceanForecastDataSource()

    @Test
    fun testCheckConnection() = runBlocking {
        // Arrange & Act
        val response = dataSource.checkConnection()

        // Assert
        assertTrue(response.status.value == 200, "Statuskoden bør være 200 OK")
    }

    @Test
    fun testFetchOceanforecastValidCoordinates() = runBlocking {
        // Arrange
        val lat = "60.10"
        val lon = "5.00" // Gyldige koordinater utenfor Bergen

        // Act
        val result = dataSource.fetchOceanForecast(lat, lon)

        // Assert
        assertTrue(result.properties.timeseries.isNotEmpty(), "Tidsserien skal inneholde data")
        result.properties.timeseries.forEach {
            assertNotNull(
                it.data.instant.details.sea_water_temperature,
                "Sjøvannstemperaturene bør ikke være null"
            )
        }
    }

    @Test
    fun testFetchOceanforecastWithInvalidCoordinates(): Unit = runBlocking {
        // Arrange
        val lat = "28.57"
        val lon = "77.09" // Ugyldige koordinater utenfor New Delhi
        // Act and Assert
        assertThrows<NoTransformationFoundException> {
            dataSource.fetchOceanForecast(lat, lon)
        }
    }
}