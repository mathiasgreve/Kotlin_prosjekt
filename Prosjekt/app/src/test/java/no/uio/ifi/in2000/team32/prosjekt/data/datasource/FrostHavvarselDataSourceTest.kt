package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Disse dataSource-testene er egentlig ikke ekte enhetstester fordi de ikke kjøres i isolasjon.
 * I stedet avhenger de av internettforbindelse og at APIene ikke er nede. Da kan man si at
 * disse testene er nærmere integrasjonstesting enn enhetstesting. Se også kilder øverst i repository-testene.
 */

class FrostHavvarselDataSourceTest {

    private val dataSource = FrostHavvarselDataSource()

    @Test
    fun testCheckConnection() = runBlocking {
        // Arrange & Act
        val response = dataSource.checkConnection()

        // Assert
        assertEquals(HttpStatusCode.OK, response.status, "Statuskoden bør være 200 OK")
    }

    @Test
    fun testFetchFrostApiResponseValidData() = runBlocking {
        // Arrange
        val lat = "59.9127"
        val lon = "10.7461" // Dette er ved Stavern
        val dist = "10"

        // Act
        val result = dataSource.fetchFrostApiResponse(lat, lon, dist)

        // Assert
        assertNotNull(result, "Forventet ikke-null resultat for gyldige koordinater")

        if (result != null) {
            assert(result.data.tseries.isNotEmpty()) { "Forventet minst én temperaturserie i responsen" }
        }

        assertTrue(
            result?.data?.tseries?.isNotEmpty() == true,
            "Forventet minst én temperaturserie i responsen"
        )
    }

    @Test
    fun testFetchFrostApiResponseNoDataFound() = runBlocking {
        // Arrange (koordinater som ikke vil gi resultater)
        val lat = "0.0000"
        val lon = "0.0000"  // Dette er ikke i Norge
        val dist = "1"

        // Act
        val result = dataSource.fetchFrostApiResponse(lat, lon, dist)

        // Assert (sjekker at resultatet er null eller tom data siden koordinatene er urealistiske
        assertNull(result, "Forventet null eller tom respons for urealistiske koordinater")
    }
}

