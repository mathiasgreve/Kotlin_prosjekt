package no.uio.ifi.in2000.team32.prosjekt.userlocation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Egendefinert Application-klasse som utfører global initialisering for appen.
 * Denne klassen er ansvarlig for opprettelse av notifikasjonskanaler som kreves
 * for appens funksjonalitet på Android Oreo (API-nivå 26) og nyere.
 *
 * Ved å opprette nødvendige notifikasjonskanaler ved appstart, sikres det at appen
 * kan sende notifikasjoner til brukeren på en kontrollert og brukervennlig måte.
 */
class LocationApp : Application() {

    /**
     * Kalt når applikasjonen starter. Denne metoden kaller [createLocationNotificationChannel]
     * for å sikre at nødvendige notifikasjonskanaler er opprettet før appen prøver å sende
     * noen notifikasjoner til brukeren.
     */
    override fun onCreate() {
        super.onCreate()
        createLocationNotificationChannel()
    }

    /**
     * Oppretter en notifikasjonskanal spesifikt for lokasjonsoppdateringer.
     * Denne metoden sjekker først om appen kjører på en enhet med Android Oreo (API-nivå 26)
     * eller nyere, siden notifikasjonskanaler er et konsept som ble introdusert i denne versjonen av Android.
     *
     * Hvis betingelsene er oppfylt, opprettes en notifikasjonskanal med lav prioritet
     * kalt "Location", som brukes til å vise notifikasjoner relatert til lokasjonsoppdateringer.
     * Kanalen har en IMPORTANCE_LOW innstilling, som betyr at notifikasjonene ikke forstyrrer brukeren
     * med lyd eller visuell forstyrrelse.
     */
    private fun createLocationNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location", // ID for notifikasjonskanalen.
                "Location", // Brukervennlig navn for kanalen.
                NotificationManager.IMPORTANCE_LOW // Prioritetsnivå for kanalen.
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

