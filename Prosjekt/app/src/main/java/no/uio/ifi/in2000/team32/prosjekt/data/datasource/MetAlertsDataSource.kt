package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.MetAlertsData

class MetAlertsDataSource : DataSource() {

    private val apiURL = "weatherapi/metalerts/2.0/current.json"

    override suspend fun checkConnection(): HttpResponse {
        return client.get("$hostURL$apiURL").body()
    }

    suspend fun fetchMetAlert(lat: Double, lon: Double): MetAlertsData {
        val requestUrl = "${hostURL}weatherapi/metalerts/2.0/current.json?lat=$lat&lon=$lon"
        return client.get(requestUrl).body()
    }
}
