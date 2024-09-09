package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.common.MapboxOptions.accessToken
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MapboxDataSource {

    private val apiKey = accessToken

    /**
     * Auto-complete in searchbar
     */
    suspend fun fetchSuggestions(query: String, country: String): List<String> =
        withContext(Dispatchers.IO) {
            try {
                val geocoding = MapboxGeocoding.builder()
                    .accessToken(apiKey)
                    .query(query)
                    .autocomplete(true)
                    .country(country)
                    .build()

                val response = geocoding.executeCall()
                response.body()?.features()?.map { it.placeName() ?: "Ukjent sted" }
                    ?: listOf("Ingen resultater funnet")

            } catch (e: Exception) {
                listOf("Feil under søk: ${e.localizedMessage}")
            }
        }

    /**
     * Finds coordinates of the relevant city
     */
    suspend fun searchCity(query: String, country: String): Point? = withContext(Dispatchers.IO) {
        try {
            val geocoding = MapboxGeocoding.builder()
                .accessToken(apiKey)
                .query(query)
                .country(country)
                .build()

            val response = geocoding.executeCall()
            response.body()?.features()?.firstOrNull()?.center()

        } catch (e: Exception) {
            null
        }
    }
}


