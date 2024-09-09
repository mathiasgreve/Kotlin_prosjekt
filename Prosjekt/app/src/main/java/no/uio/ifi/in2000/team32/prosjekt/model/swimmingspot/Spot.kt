package no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot

/**
 * Models a swimming spot
 */
data class Spot(
    val lat: String,
    val lon: String,
    val swimmingSpotName: String? = null,
)