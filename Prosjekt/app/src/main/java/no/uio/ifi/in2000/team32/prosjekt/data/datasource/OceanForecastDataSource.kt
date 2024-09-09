package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast.OceanForecast


class OceanForecastDataSource : DataSource() {

    override suspend fun checkConnection(): HttpResponse {
        return client.get("${hostURL}weatherapi/oceanforecast/2.0/complete?lat=60.10&lon=5")
    }

    suspend fun fetchOceanForecast(lat: String, lon: String): OceanForecast {
        val requestURL =
            client.get("${hostURL}weatherapi/oceanforecast/2.0/complete?lat=${lat}&lon=${lon}")
        return requestURL.body()
    }
}

