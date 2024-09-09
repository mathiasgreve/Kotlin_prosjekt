package no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot

data class SwimmingSpotData(
    val data: Data
)

data class Data(
    val tstype: String,
    val tseries: List<TemperatureSeries>
)

data class TemperatureSeries(
    val header: Header,
    val observations: List<Observation>?
)

data class Header(
    val id: Id,
    val extra: Extra
)

data class Id(
    val buoyid: String,
    val parameter: String,
    val source: String
)

data class Extra(
    val name: String,
    val pos: Position
)

data class Position(
    val lat: String,
    val lon: String
)

data class Observation(
    val time: String?,
    val body: Body?
)

data class Body(
    val value: String?
)