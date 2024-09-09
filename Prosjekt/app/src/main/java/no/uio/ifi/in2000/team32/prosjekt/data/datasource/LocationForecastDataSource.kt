package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import no.uio.ifi.in2000.team32.prosjekt.model.locationforecast.Feature


class LocationForecastDataSource : DataSource() {

    override suspend fun checkConnection(): HttpResponse {
        return client.get("${hostURL}weatherapi/locationforecast/2.0/compact?lat=60.10&lon=9.58")
    }

    suspend fun fetchLocationForecast(latitude: String, longitude: String): Feature {
        val requestURL =
            "${hostURL}weatherapi/locationforecast/2.0/complete?lat=${latitude}&lon=${longitude}"
        return client.get(requestURL).body()
    }
}