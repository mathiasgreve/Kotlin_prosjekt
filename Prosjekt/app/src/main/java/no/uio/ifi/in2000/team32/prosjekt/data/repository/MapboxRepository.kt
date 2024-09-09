package no.uio.ifi.in2000.team32.prosjekt.data.repository

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team32.prosjekt.data.datasource.MapboxDataSource


class MapboxRepository {
    private val dataSource = MapboxDataSource()

    /**
     * Fetches location suggestions based on the query and country.
     *
     * @param query The search query for suggestions.
     * @param country The country to limit the search.
     * @return A list of location suggestions.
     */
    suspend fun getSuggestions(query: String, country: String): List<String> {
        return dataSource.fetchSuggestions(query, country)
    }

    /**
     * Searches for a city location based on the query and country.
     *
     * @param query The search query for the city.
     * @param country The country to limit the search.
     * @return A Point object representing the city's location, or null if not found.
     */
    suspend fun getCityLocation(query: String, country: String): Point? {
        return dataSource.searchCity(query, country)
    }
}


