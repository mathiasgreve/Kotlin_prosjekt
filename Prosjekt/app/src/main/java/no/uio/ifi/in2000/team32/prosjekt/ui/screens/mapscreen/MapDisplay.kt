package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import no.uio.ifi.in2000.team32.prosjekt.R
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.bottomsheet.BottomSheet
import java.util.Locale


@OptIn(MapboxExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun MapDisplay(
    mapViewModel: MapViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    networkIsAvailable: Boolean,
    userLocation: State<Resource<Point>>
) {
    val relevantSwimmingSpots by mapViewModel.relevantSwimmingSpots.collectAsState()

    val showWeatherForecast = remember { mutableStateOf(false) }
    var pinnedLocation by remember { mutableStateOf<Spot?>(null) }

    var showPin by remember { mutableStateOf(false) }

    //Kart
    Box(modifier = Modifier.fillMaxSize()) {
        MapboxMap(mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = "mapbox://styles/fredrvog/clw27q5u1019h01qugx6ldx7m",
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(11.4953, 64.0150)) // Oppstart midt i Norge
                    .zoom(4.0) // Zoomet ut for å se hele Norge
                    .build()
            )
        }, onMapLongClickListener = { p ->
            pinnedLocation = (Spot(
                String.format(Locale.US, "%.4f", p.latitude()),
                String.format(Locale.US, "%.4f", p.longitude())
            )
                    )

            showPin = true
            showWeatherForecast.value = true
            mapViewModel.updateWeatherForecast(
                Spot(
                    p.latitude().toString(), p.longitude().toString()
                )
            )

            mapViewModel.updateAlerts(Spot(p.latitude().toString(), p.longitude().toString()))
            mapViewModel.mapViewportState.flyTo(CameraOptions.Builder().bearing(0.0)
                .center(Point.fromLngLat(p.longitude(), p.latitude() - 0.0155))
                .zoom(12.0)
                .build(), MapAnimationOptions.mapAnimationOptions {
                duration(2000L)
            })
            true
        },

            mapViewportState = mapViewModel.mapViewportState
        ) {

            // strandmarkinger
            relevantSwimmingSpots.let { resource ->
                if (resource is Resource.Success) {
                    val pointAnnotations = resource.data.map { spot ->
                        PointAnnotationOptions().withPoint(
                            Point.fromLngLat(
                                spot.lon.toDouble(), spot.lat.toDouble()
                            )
                        ).withIconImage(loadBitmapFromDrawable(drawableResId = R.mipmap.badested))
                            .withIconSize(1.0)
                            .withData(JsonPrimitive(spot.swimmingSpotName)) // sender med swimmingSpotName for å kunne identifisere hvilken badeplass som er trykket på
                    }

                    PointAnnotationGroup(annotations = pointAnnotations, onClick = { annotation ->
                        showWeatherForecast.value = true
                        val clickedSpot = Spot(
                            annotation.point.latitude().toString(),
                            annotation.point.longitude().toString(),
                            annotation.getData()?.asString
                        )
                        mapViewModel.updateWeatherForecast(clickedSpot)
                        mapViewModel.updateAlerts(clickedSpot)


                        Log.e("Nå er det fest på badeplassen!", "")
                        mapViewModel.mapViewportState.flyTo(CameraOptions.Builder().bearing(0.0)
                            .center(
                                Point.fromLngLat(
                                    annotation.point.longitude(),
                                    annotation.point.latitude() - 0.0125
                                )
                            ).zoom(12.0)
                            .build(), MapAnimationOptions.mapAnimationOptions {
                            duration(2000L)
                        })

                        true
                    })
                }
            }

            // pinpoint hvor som helst på kartet
            if (showPin) {
                if (pinnedLocation != null) {
                    val pinLoc = pinnedLocation as Spot

                    // PointAnnotation er en synlig markør på kartet
                    PointAnnotation(point = Point.fromLngLat(
                        pinLoc.lon.toDouble(), pinLoc.lat.toDouble()
                    ),
                        iconImageBitmap = loadBitmapFromDrawable(drawableResId = R.mipmap.pin_trans_2),
                        iconSize = 1.2,
                        onClick = {
                            showWeatherForecast.value = true
                            mapViewModel.updateWeatherForecast(
                                Spot(
                                    pinLoc.lat, pinLoc.lon
                                )
                            )
                            mapViewModel.updateAlerts(
                                Spot(
                                    pinLoc.lat, pinLoc.lon
                                )
                            )

                            Log.d("showPin", "Now showing pin")

                            mapViewModel.mapViewportState.flyTo(CameraOptions.Builder().bearing(0.0)
                                .center(
                                    Point.fromLngLat(
                                        it.point.longitude(), it.point.latitude() - 0.0125
                                    )
                                ).zoom(12.0)
                                .build(), MapAnimationOptions.mapAnimationOptions {
                                duration(2000L)
                            })

                            true
                        })
                }

            }

            //Her vises Brukerns bakgrunnslokasjon.
            when (userLocation.value) {
                is Resource.Success -> {
                    val userLoc = (userLocation.value as Resource.Success<Point>).data

                    PointAnnotation(
                        point = userLoc,
                        iconImageBitmap = loadBitmapFromDrawable(drawableResId = R.drawable.userpoint2),
                        iconSize = 0.5
                    )
                }

                else -> Log.i("MapDisplay", "userLocation does not contain data")
            }
        }
    }

    if (showWeatherForecast.value) {
        if (networkIsAvailable) {
            BottomSheet(mapViewModel, navController, showWeatherForecast)
        } else {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar("Noe gikk galt. Ingen internett-tilgang")
                showWeatherForecast.value = false
            }
        }
    }

}


@Composable
fun loadBitmapFromDrawable(drawableResId: Int): Bitmap {
    val context = LocalContext.current
    return BitmapFactory.decodeResource(context.resources, drawableResId)
}
