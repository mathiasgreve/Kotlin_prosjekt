package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.SwimmingSpotData


class FrostHavvarselDataSource :
    DataSource() {          //Arver HTTP-client fra abstact class for å unngå redundans

    private val frostHavvarselBaseURL =
        "https://havvarsel-frost.met.no/api/v1/obs/badevann/get?incobs=false&nearest="

    // Metode som henter data fra Frost
    override suspend fun checkConnection(): HttpResponse {
        return client.get("https://havvarsel-frost.met.no/api/v1/obs/badevann/get?incobs=false&time=latest&parameters=temperature")     //Uten proxy: Funker
    }

    suspend fun fetchFrostApiResponse(lat: String, lon: String, dist: String): SwimmingSpotData? {
        return try {
            client.get("$frostHavvarselBaseURL{\"maxdist\":$dist,\"maxcount\":99999,\"points\":[{\"lon\":$lon,\"lat\":$lat}]}")
                .body()
        } catch (e: Exception) {
            return null
        }
    }
}


