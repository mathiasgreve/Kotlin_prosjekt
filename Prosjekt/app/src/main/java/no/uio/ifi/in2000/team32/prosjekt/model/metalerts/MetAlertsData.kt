package no.uio.ifi.in2000.team32.prosjekt.model.metalerts

// "Typo in word"-warnings kan ikke omgås, ettersom dette er slik API-et er skrevet

data class MetAlertsData(
    val features: List<Feature>,
    val lang: String,
    val lastChange: String,
    val type: String
)


data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    val `when`: When
)


data class Geometry(
    val coordinates: List<List<List<Any>>>,
    val type: String
)


// Values with arrow are used by Alert data class. If we have time,
// values without arrow should be deleted, to ensure less memory usage, and faster creation of Alerts
data class Properties(
    val altitude_above_sea_level: Int,
    val area: String,
    val awarenessResponse: String,                //<-----------------
    val awarenessSeriousness: String,                //<-----------------
    val awareness_level: String,                //<-----------------
    val awareness_type: String,                //<-----------------
    val ceiling_above_sea_level: Int,
    val certainty: String,
    val consequences: String,                //<-----------------
    val contact: String,                //<-----------------
    val county: List<String>,
    val description: String,                //<-----------------
    val event: String,
    val eventAwarenessName: String,                //<-----------------
    val eventEndingTime: String?,
    val geographicDomain: String,
    val id: String,
    val instruction: String,                //<-----------------
    val municipality: List<String>,
    val resources: List<Resource>,                //<-----------------
    val riskMatrixColor: String,                //<-----------------
    val severity: String,                //<-----------------
    val status: String,
    val title: String,
    val triggerLevel: String,
    val type: String,
    val web: String                //<-----------------
)

data class When(
    val interval: List<String>
)

data class Resource(
    val description: String,
    val mimeType: String,
    val uri: String
)