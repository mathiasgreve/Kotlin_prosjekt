package no.uio.ifi.in2000.team32.prosjekt.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson


abstract class DataSource {

    protected val hostURL: String = "https://gw-uio.intark.uh-it.no/in2000/"
    protected val client = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }

        defaultRequest {
            // Her kan du eventuelt sette en base-URL, men husk at noen kall kan trenge fullstendig URL
            header("X-Gravitee-API-Key", "c64f7335-6202-4c56-a200-cef7b4a62bac") // 'Sync Project with Gradle Files' might fix this 'BuildConfig' "bug" (or 'Clean Project' + 'Rebuild Project')
        } // Appen burde kunne kjøre selv om 'BuildConfig' skulle være "rød"
    }

    abstract suspend fun checkConnection(): HttpResponse
}
