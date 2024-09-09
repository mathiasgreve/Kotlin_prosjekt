package no.uio.ifi.in2000.team32.prosjekt.data.repository

import io.ktor.utils.io.errors.IOException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.MetAlertsDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Feature
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Geometry
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.MetAlertsData
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Properties
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.When
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
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

class MetAlertsRepositoryTest {

    private lateinit var mockDataSource: MetAlertsDataSource
    private lateinit var repository: MetAlertsRepository

    @BeforeEach
    fun setUp() {
        mockDataSource = mockk()
        repository = MetAlertsRepository(mockDataSource)
    }

    @Test
    fun `findAlerts returns empty list when no alerts are present`() = runTest {
        // Arrange
        val noAlertData = createEmptyAlertData()
        coEvery { mockDataSource.fetchMetAlert(any(), any()) } returns noAlertData

        // Act
        val alerts = repository.findAlerts(58.0, 14.0).listOfAlerts

        // Assert
        assertTrue(alerts.isEmpty(), "Expected no alerts to be returned when none are present")
    }

    @Test
    fun `findAlerts returns alerts when present`() = runTest {
        // Arrange
        val alertData = createMockAlertData()
        coEvery { mockDataSource.fetchMetAlert(any(), any()) } returns alertData

        // Act
        val alerts = repository.findAlerts(58.0, 14.0).listOfAlerts

        // Assert
        assertFalse(alerts.isEmpty(), "Expected a non-empty list")
        assertNotNull(alerts.find { it.consequences == "Vegetasjon kan lett antennes og store områder kan bli berørt." })
    }

    @Test
    fun `findAlerts handles exceptions gracefully`() = runTest {
        // Arrange
        val lat = 58.0
        val lon = 14.0
        coEvery {
            mockDataSource.fetchMetAlert(
                any(),
                any()
            )
        } throws IOException("Failed to fetch data")

        // Act
        val result = runCatching { repository.findAlerts(lat, lon).listOfAlerts }

        // Assert
        assertDoesNotThrow("Should not throw exception") { result.getOrThrow() }
        assertEquals(0, result.getOrThrow().size, "Should return an empty list if API-failure")
    }

    @Test
    fun `findAlerts sorts list correctly`() = runTest {
        // Arrange
        coEvery { mockDataSource.fetchMetAlert(any(), any()) } returns createMockAlertData()

        // Act
        val alerts = repository.findAlerts(58.0, 14.0).listOfAlerts

        // Assert
        assertTrue(
            alerts.first() > alerts.last(),
            "listOfAlerts should be sorted from most to least dangerous"
        )
    }

    /**
     * Creates mock data for MetAlerts to be used in tests.
     *
     * This function generates a `MetAlertsData` object populated with hardcoded alert data
     * to simulate real alert data responses from the MET alerts API.
     *
     * @return A `MetAlertsData` object containing mock alert data.
     */
    private fun createMockAlertData(): MetAlertsData {
        return MetAlertsData(
            features = listOf(
                Feature(
                    geometry = Geometry(
                        coordinates = listOf(
                            listOf(listOf(14.3124, 65.1374)),
                            // Flere koordinater her ...
                        ),
                        type = "Polygon"
                    ),
                    properties = Properties(
                        altitude_above_sea_level = 0,
                        area = "Snøfrie områder i deler av Nordland",
                        awarenessResponse = "Følg med",
                        awarenessSeriousness = "Utfordrende situasjon",
                        awareness_level = "2; yellow; Moderate",
                        awareness_type = "8; forest-fire",
                        ceiling_above_sea_level = 183,
                        certainty = "Observed",
                        consequences = "Vegetasjon kan lett antennes og store områder kan bli berørt.",
                        contact = "https://www.met.no/kontakt-oss",
                        county = listOf("18"),
                        description = "Lokal gress- og lyngbrannfare inntil det kommer tilstrekkelig nedbør.",
                        event = "forestFire",
                        eventAwarenessName = "Skogbrannfare",
                        eventEndingTime = null,
                        geographicDomain = "land",
                        id = "2.49.0.1.578.0.20240424064440.035",
                        instruction = "Ikke bruk ild. Følg lokale myndigheters instruksjoner.",
                        municipality = listOf("1804", "1806"),
                        resources = listOf(
                            Resource(
                                description = "CAP file",
                                mimeType = "application/xml",
                                uri = "https://api.met.no/weatherapi/metalerts/2.0/current?cap=2.49.0.1.578.0.20240424064440.035"
                            )
                        ),
                        riskMatrixColor = "Yellow",
                        severity = "Moderate",
                        status = "Actual",
                        title = "Skogbrannfare",
                        triggerLevel = "high",
                        type = "Update",
                        web = "https://www.met.no/vaer-og-klima/ekstremvaervarsler"
                    ),
                    type = "Feature",
                    `when` = When(
                        interval = listOf(
                            "2024-04-22T22:00:00+00:00",
                            "2024-04-26T18:00:00+00:00"
                        )
                    )
                ),
                Feature(
                    geometry = Geometry(
                        coordinates = listOf(
                            listOf(listOf(14.3124, 65.1374)),
                            // Flere koordinater her ...
                        ),
                        type = "Polygon"
                    ),
                    properties = Properties(
                        altitude_above_sea_level = 0,
                        area = "Deler av Innladet",
                        awarenessResponse = "Sikre verdiene",
                        awarenessSeriousness = "Ekstrem situasjon",
                        awareness_level = "4; red; Extreme",
                        awareness_type = "10; rain",
                        ceiling_above_sea_level = 2743,
                        certainty = "Likely",
                        consequences = "TEST TEST TEST Fare for overvann i tettbygde områder. Se www.varsom.no for mer informasjon. Stor fare for stengte veier og/eller overvann ved bekke- og elveløp. Mange reiser vil kunne få lenger reisetid. Vanskelige kjøreforhold grunnet overvann og fare for vannplaning. En del steder vil midlertidig kunne miste veiforbindelsen, gjerne flere dager. Bygninger/strukturer kan kollapse som følge av nedbøren. Fare for liv om man havner i flomelver/ annen strøm. Fare for lengre strømbrudd. ",
                        contact = "https://www.met.no/kontakt-oss",
                        county = listOf("34", "32"),
                        description = "TEST TEST TEST Det ventes ekstremt mye regn, lokalt 80-100 mm/12t. Hendelsen vil mange steder være blant de kraftigste de siste 25 år. Hendelsen ventes å bli noe sterkere enn Harry på Sørlandet.",
                        event = "rain",
                        eventAwarenessName = "TEST TEST TEST Ekstremt mye regn",
                        eventEndingTime = "2024-05-08T12:00:00+00:00",
                        geographicDomain = "land",
                        id = "2.49.0.1.578.0.20240506114932.065",
                        instruction = "TEST TEST TEST Vurder behov for forebyggende tiltak. Følg lokale myndigheters instruksjoner, og råd fra beredskapsmyndigheter. Behov for beredskap skal vurderes fortløpende av beredskapsaktører. Unngå å kjøre i overvann, uten å vite hvor dypt det er. Vurder nøye om reisen faktisk er nødvendig. Gjør hjemmet klar for et potensielt lengre strømbrudd. ",
                        municipality = listOf(
                            "3240",
                            "3242",
                            "3401",
                            "3403",
                            "3405",
                            "3407",
                            "3411",
                            "3412",
                            "3413",
                            "3414",
                            "3415",
                            "3417",
                            "3418",
                            "3419",
                            "3420",
                            "3421",
                            "3422",
                            "3423",
                            "3424",
                            "3436",
                            "3438",
                            "3439",
                            "3440",
                            "3441",
                            "3442",
                            "3443",
                            "3446",
                            "3447",
                            "3448"
                        ),
                        resources = listOf(
                            Resource(
                                description = "CAP file",
                                mimeType = "application/xml",
                                uri = "https://api.met.no/weatherapi/metalerts/2.0/test?cap=2.49.0.1.578.0.20240506114932.065"
                            )
                        ),
                        riskMatrixColor = "Red",
                        severity = "Extreme",
                        status = "Test",
                        title = "TEST TEST TEST Ekstremt mye regn, rødt nivå, Deler av Innladet, 2024-05-07T12:00:00+00:00, 2024-05-08T12:00:00+00:00",
                        triggerLevel = "80mm/24h",
                        type = "Alert",
                        web = "https://www.met.no/vaer-og-klima/ekstremvaervarsler-og-andre-farevarsler/vaerfenomener-som-kan-gi-farevarsel-fra-met/farevarsel-for-nedbor"
                    ),
                    type = "Feature",
                    `when` = When(
                        interval = listOf(
                            "2024-05-07T12:00:00+00:00",
                            "2024-05-08T12:00:00+00:00"
                        )
                    )
                )
            ),
            lang = "no",
            lastChange = "2024-04-24T19:41:09+00:00",
            type = "FeatureCollection"
        )
    }

    /**
     * Creates empty mock data for MetAlerts to be used in tests.
     *
     * This function generates an empty `MetAlertsData` object to simulate scenarios where no alerts
     * are present. This is useful for testing edge cases and ensuring the application handles
     * the absence of alert data gracefully.
     *
     * @return A `MetAlertsData` object containing no alert data.
     */
    private fun createEmptyAlertData(): MetAlertsData {
        return MetAlertsData(
            features = listOf(),
            lang = "no",
            lastChange = "2024-04-24T19:07:10+00:00",
            type = "FeatureCollection"
        )
    }
}

/*
Bruk av 'coEvery' sikrer at suspend funksjoner håndteres riktig ved mocking.
Testene sjekker om repository-metodene returnerer korrekte data
eller håndterer fraværet av data som forventet.

For å være sikker på at alt fungerer som det skal, bør vi også inkludere
test for feilhåndtering og verifisere at koden korrekt håndterer
eventuelle feil som kan oppstå når man interagerer med datakilden.

Vi har testet:
1) Testing for ingen farevarsler til stede
2) Testing for tilstedeværelse av farevarsler
3) Feilhåndtering
 */