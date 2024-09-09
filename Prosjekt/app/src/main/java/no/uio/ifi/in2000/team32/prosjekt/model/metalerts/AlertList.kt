package no.uio.ifi.in2000.team32.prosjekt.model.metalerts

data class Alert(
    val area: String,
    val awarenessResponse: String,
    val awarenessSeriousness: String,
    val awarenessLevel: String,
    val awarenessType: String,
    val consequences: String,
    val contact: String,
    val description: String,
    val event: String,
    val eventAwarenessName: String,
    val instruction: String,
    val resources: List<Resource>,
    val severity: String,
    val moreInfoURL: String,
    val riskColor: String,
    val interval: List<String>
) : Comparable<Alert> {
    private val severityLevel: Int = when (severity) {
        "Extreme" -> 3
        "Severe" -> 2
        "Moderate" -> 1
        "Minor" -> 0
        else -> -1 // Should preferably not happen
    }

    override fun compareTo(other: Alert): Int {
        return this.severityLevel.compareTo(other.severityLevel)
    }
}

data class AlertList(
    val listOfAlerts: MutableList<Alert>,
    val lat: String,
    val lon: String
)