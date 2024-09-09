package no.uio.ifi.in2000.team32.prosjekt.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.maps.MapboxExperimental
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team32.prosjekt.R
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.MapViewModel


@OptIn(MapboxExperimental::class)
@Composable
fun ShowSwimmingSpotsButton(mapViewModel: MapViewModel) {
    val center = mapViewModel.mapViewportState.cameraState.center
    val spot = Spot(lat = center.latitude().toString(), lon = center.longitude().toString())

    FloatingActionButton(
        onClick = { mapViewModel.updateRelevantSwimmingSpots(spot) },
        content = {
            Text(
                text = "Finn badeplasser",
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        },
    )
}


@Composable
fun UserLocationButton(
    mapViewModel: MapViewModel,
    snackbarHostState: SnackbarHostState,
    hasLocation: Boolean
) {
    val scope = rememberCoroutineScope()

    val colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.White) else null

    FloatingActionButton(
        modifier = Modifier.alpha(if (hasLocation) 1f else 0.6f),
        content = {
            Image(
                painter = painterResource(id = R.drawable.target02),
                contentDescription = "Finn min posisjon",
                modifier = Modifier.scale(2f),
                colorFilter = colorFilter,
            )
        },
        onClick = {
            when (hasLocation) {
                true -> mapViewModel.moveToUserLocation()

                false -> scope.launch {
                    snackbarHostState.showSnackbar("Finner ikke din lokasjon")
                }
            }
        }
    )

}
