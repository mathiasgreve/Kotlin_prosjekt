package no.uio.ifi.in2000.team32.prosjekt.userlocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Sjekker om appen har nødvendige lokasjonstillatelser.
 * Denne funksjonen sjekker om enten grov eller fin lokasjonstillatelse er innvilget av brukeren.
 * Tillatelsene som sjekkes er definert i Manifest.permission.ACCESS_COARSE_LOCATION og
 * Manifest.permission.ACCESS_FINE_LOCATION.
 *
 * @return [Boolean] `true` hvis en av tillatelsene er innvilget, `false` ellers.
 */
fun Context.hasLocationPermission(): Boolean {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    return permissions.any {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}
