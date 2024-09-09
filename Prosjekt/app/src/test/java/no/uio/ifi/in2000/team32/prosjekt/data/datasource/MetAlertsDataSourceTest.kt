package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Disse dataSource-testene er egentlig ikke ekte enhetstester fordi de ikke kjøres i isolasjon.
 * I stedet avhenger de av internettforbindelse og at APIene ikke er nede. Da kan man si at
 * disse testene er nærmere integrasjonstesting enn enhetstesting. Se også kilder øverst i repository-testene.
 */

class MetAlertsDataSourceTest {

    private val metAlertsDataSource = MetAlertsDataSource()

    @Test
    fun testCheckConnection() = runBlocking {
        val response: HttpResponse = metAlertsDataSource.checkConnection()
        assertEquals(HttpStatusCode.OK, response.status, "Statuskoden bør være 200 OK")
        assertEquals(
            "https://gw-uio.intark.uh-it.no/in2000/weatherapi/metalerts/2.0/current.json",
            response.request.url.toString(),
            "URL-en bør matche den forespurte"
        )
    }
}