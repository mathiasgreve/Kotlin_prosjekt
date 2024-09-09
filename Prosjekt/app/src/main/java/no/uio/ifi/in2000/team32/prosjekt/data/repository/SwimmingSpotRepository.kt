package no.uio.ifi.in2000.team32.prosjekt.data.repository

import no.uio.ifi.in2000.team32.prosjekt.data.datasource.FrostHavvarselDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Position
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.TemperatureSeries


class SwimmingSpotRepository(private val frostHavvarselDataSource: FrostHavvarselDataSource) {

    /**
     * Returns a list of all (found) SwimmingSpots within the given distance from the given coordinates
     * @param lat: String representing the latitude for the search
     * @param lon: String representing the longitude for the search
     * @param dist: String representing the max acceptable distance from the given coordinates
     * @return A List of Spot objects, with each spot object representing the coordinates and name
     * of a swimming spot
     */
    suspend fun getSwimmingSpots(lat: String, lon: String, dist: String): List<Spot> {
        val spotsAtCorrectCoords =
            responseAtCoords(lat = lat, lon = lon, dist)

        val spotsAtIncorrectCoords =
            responseAtCoords(lat = lon, lon = lat, dist) // Met blander ofte lat og lon

        return createSwimmingSpots(spotsAtCorrectCoords + spotsAtIncorrectCoords)
    }

    /**
     * Helper method for getSwimmingSpots(). Creates a list of spots based on a list of
     * TemperatureSeries objects
     * @param spots: List<TemperatureSeries> object, representing every place to make a Spot object for
     * @return A List of Spot objects, with each spot object representing the coordinates and name
     * of a swimming spot
     */
    private fun createSwimmingSpots(spots: List<TemperatureSeries>): List<Spot> {
        val swimmingSpots: MutableList<Spot> = mutableListOf()

        spots.forEach { spot -> // Iterates through all relevant coordinates
            val correctedPos =
                correctCoords(spot.header.extra.pos) // I tilfelle lat og lon er swappet

            swimmingSpots.add(
                Spot(
                    // Creates a BathingArea element for the given coordinates
                    lat = correctedPos.lat,
                    lon = correctedPos.lon,
                    swimmingSpotName = spot.header.extra.name,
                )
            )
        }

        return swimmingSpots
    }

    /**
     * Helper method returning the a List<TemperatureSeries> for a given coordinate
     * @param lat: String representing the latitude for the search
     * @param lon: String representing the longitude for the search
     * @param dist: String representing the max acceptable distance from the given coordinates
     * @return A List<TemperatureSeries> containing the relevant data from the given query-arguments
     */
    private suspend fun responseAtCoords(
        lat: String,
        lon: String,
        dist: String
    ): List<TemperatureSeries> {
        return frostHavvarselDataSource.fetchFrostApiResponse(lat = lat, lon = lon, dist)
            ?.data?.tseries ?: listOf()
    }

    /**
     * The API we use frequently mixes up latitude and longitude, leading to spots that should
     * for example be in Oslo, instead being in parts of India. This method corrects for these mistakes
     * @param pos: A Position object, containing a latitude and longitude
     * @return A new Position object where lat and lon have been swapped in (lat < lon)
     */
    private fun correctCoords(pos: Position): Position {
        val lat = pos.lat
        val lon = pos.lon

        if (lat.toDouble() < lon.toDouble()) { // Swapper eventuell feil (lon,lat) til (lat,lon)
            return Position(lat = lon, lon = lat)
        }

        return Position(lat = lat, lon = lon) // Ellers returneres det som det er.
    }
}