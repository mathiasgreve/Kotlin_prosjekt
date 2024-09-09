package no.uio.ifi.in2000.team32.prosjekt.data.repository

import no.uio.ifi.in2000.team32.prosjekt.data.datasource.MetAlertsDataSource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Alert
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.AlertList
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Feature

/**
 * Repository class responsible for fetching and processing alert data from the Met Alerts API.
 *
 * @property metAlertsDataSource The data source used to fetch alert data.
 */
class MetAlertsRepository(private val metAlertsDataSource: MetAlertsDataSource) {

    /**
     * Fetches and processes alert data for a given location.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return An [AlertList] containing the processed alert data.
     */
    suspend fun findAlerts(lat: Double = 0.0, lon: Double = 0.0): AlertList {
        val features: List<Feature>? =
            try {
                metAlertsDataSource.fetchMetAlert(lat, lon).features
            } catch (e: Exception) {
                null
            }


        val alerts = mutableListOf<Alert>()

        features?.forEach {
            alerts.add(
                Alert(
                    area = it.properties.area,
                    awarenessResponse = it.properties.awarenessResponse,
                    awarenessSeriousness = it.properties.awarenessSeriousness,
                    awarenessLevel = it.properties.awareness_level,
                    awarenessType = it.properties.awareness_type,
                    consequences = it.properties.consequences,
                    contact = it.properties.contact,
                    description = it.properties.description,
                    event = it.properties.event,
                    eventAwarenessName = it.properties.eventAwarenessName,
                    instruction = it.properties.instruction,
                    resources = it.properties.resources,
                    severity = it.properties.severity,
                    moreInfoURL = it.properties.web,
                    riskColor = it.properties.riskMatrixColor,
                    interval = it.`when`.interval
                )
            )
        }
        alerts.sortDescending()

        return AlertList(alerts, lat.toString(), lon.toString())
    }
}