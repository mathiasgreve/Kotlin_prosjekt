package no.uio.ifi.in2000.team32.prosjekt.model.locationforecast

// "Typo in word"-warnings kan ikke omgås, ettersom dette er slik API-et er skrevet

data class Feature(
    val type: String?,
    val geometry: Geometry,
    val properties: Properties
)


data class Geometry(
    val type: String,
    val coordinates: List<Double>
)


data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

data class Meta(
    val updated_at: String,
    val units: Map<String, String>
)

data class TimeSeries(
    val time: String,
    val data: Data
)


data class Data(
    val instant: InstantDetails,
    val next_12_hours: ForecastPeriod?,
    val next_1_hours: ForecastPeriod?,
    val next_6_hours: ForecastPeriod?
)

data class InstantDetails(
    val details: Map<String, Double>
)


data class ForecastPeriod(
    val summary: Summary,
    val details: Map<String, Double>
)

data class Summary(
    val symbol_code: String
)