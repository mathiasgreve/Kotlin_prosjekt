package no.uio.ifi.in2000.team32.prosjekt.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.FrostHavvarselDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Body
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Data
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Extra
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Header
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Id
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Observation
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Position
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.SwimmingSpotData
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.TemperatureSeries
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

/** For unit tests it is useful to have consistent and controlled input data. Sources for unit testing with JUnit5 and MockK:
 *
 * https://developer.android.com/training/testing/local-tests
 * https://junit.org/junit5/ and https://github.com/mannodermaus/android-junit5
 * https://mockk.io/ANDROID.html, the library used to create controlled input data by generating mock objects.
 */

class SwimmingSpotRepositoryTest {

    private lateinit var mockDataSource: FrostHavvarselDataSource
    private lateinit var repository: SwimmingSpotRepository

    @BeforeEach
    fun setUp() {
        mockDataSource = mockk()
        repository = SwimmingSpotRepository(mockDataSource)
    }

    @Test
    fun `getSwimmingSpots returns correct spots`() = runTest {
        // Arrange
        val correctData = createMockTemperatureSeries("58.0", "7.0", "Sørlandet Beach")
        val incorrectData =
            createMockTemperatureSeries("7.0", "58.0", "Oslo Beach") // Lat og Lon byttet
        coEvery {
            mockDataSource.fetchFrostApiResponse(
                "58.0",
                "7.0",
                "10"
            )
        } returns createMockSwimmingSpotData(listOf(correctData))
        coEvery {
            mockDataSource.fetchFrostApiResponse(
                "7.0",
                "58.0",
                "10"
            )
        } returns createMockSwimmingSpotData(listOf(incorrectData))

        // Act
        val spots = repository.getSwimmingSpots("58.0", "7.0", "10")

        // Assert
        assertEquals(2, spots.size)
        assertTrue(spots.any { it.swimmingSpotName == "Sørlandet Beach" && it.lat == "58.0" && it.lon == "7.0" })
        assertTrue(spots.any { it.swimmingSpotName == "Oslo Beach" && it.lat == "58.0" && it.lon == "7.0" }) // Corrected coordinates
    }

    @Test
    fun `getSwimmingSpots handles API failures gracefully`() = runTest {
        // Arrange
        coEvery { mockDataSource.fetchFrostApiResponse(any(), any(), any()) } returns null

        // Act
        val result = runCatching { repository.getSwimmingSpots("invalid", "coordinates", "10") }

        // Assert
        assertDoesNotThrow("Should handle API failure") { result.getOrThrow() }
        assertTrue(result.getOrThrow().isEmpty(), "Expected an empty list on API failure")
    }

    @Test
    fun `getSwimmingSpots handles invalid coordinates gracefully`() = runTest {
        // Arrange
        coEvery { mockDataSource.fetchFrostApiResponse("999", "999", "10") } returns null

        // Act
        val spots = repository.getSwimmingSpots("999", "999", "10")

        // Assert
        assertTrue(spots.isEmpty(), "Expected no spots to be returned for invalid coordinates")
    }

    /**
     * Creates a mock TemperatureSeries object for use in tests.
     *
     * This function generates a `TemperatureSeries` object with hardcoded observation data,
     * using the provided latitude, longitude, and name.
     *
     * @param lat The latitude for the position of the temperature series.
     * @param lon The longitude for the position of the temperature series.
     * @param name The name associated with the temperature series.
     * @return A `TemperatureSeries` object containing mock temperature data.
     */
    private fun createMockTemperatureSeries(
        lat: String,
        lon: String,
        name: String
    ): TemperatureSeries {
        return TemperatureSeries(
            header = Header(
                id = Id(buoyid = "123", parameter = "temperature", source = "testSource"),
                extra = Extra(
                    name = name,
                    pos = Position(lat = lat, lon = lon)
                )
            ),
            observations = listOf(
                Observation(
                    time = "2024-04-01T12:00:00Z",
                    body = Body(value = "15")
                )
            )
        )
    }

    /**
     * Creates a mock SwimmingSpotData object for use in tests.
     *
     * This function generates a `SwimmingSpotData` object with the provided list of `TemperatureSeries`.
     *
     * @param temperatureSeries The list of `TemperatureSeries` to include in the mock data.
     * @return A `SwimmingSpotData` object containing the provided temperature series data.
     */
    private fun createMockSwimmingSpotData(temperatureSeries: List<TemperatureSeries>): SwimmingSpotData {
        return SwimmingSpotData(
            data = Data(
                tstype = "Forecast",
                tseries = temperatureSeries
            )
        )
    }
}

/*
1.Bruk av '@BeforeEach' og '@Test' fra JUnit5:
Dette sikrer bedre støtte for testlivssyklusen og er
mer naturlig for JUnit5-rammeverket.

2.Innsetting av 'MockK':
MockK er en Kotlin-spesifikk mocking-rammeverk som gir bedre
integrasjon med Kotlins funksjoner som coroutines og
extension functions.

3.Håndtering av suspend funksjoner med 'runTest':
Dette er en del av 'kotlinx-coroutines-test', som gir en
måte å teste suspend funksjoner i en kontrollert coroutine-kontekst.

4.Bruk av 'coEvery' for å sette opp mock-atferd:
Dette reflekterer at datakilde funksjonene er suspend funksjoner og bruker
coroutine-støtte i MockK.

5.Error handling med 'runCatching':
Dette er en Kotlin-idiomatisk måte å håndtere potensielle feil på, noe
som gjør koden renere og mer robust.
 */