package no.uio.ifi.in2000.team32.prosjekt.model.oceanforecast

// "Typo in word"-warnings kan ikke omgås, ettersom dette er slik API-et er skrevet

data class OceanForecast(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val meta: SeaWaterMeta,
    val timeseries: List<TimeSeries>
)

data class SeaWaterMeta(
    val updated_at: String,
    val units: Map<String, String>
)

data class TimeSeries(
    val time: String,
    val data: SeaWaterData
)

data class SeaWaterData(
    val instant: Instant
)

data class Instant(
    val details: Details
)

data class Details(
    val sea_surface_wave_from_direction: Double,
    val sea_surface_wave_height: Double,
    val sea_water_speed: Double,
    val sea_water_temperature: Double,
    val sea_water_to_direction: Double
)
