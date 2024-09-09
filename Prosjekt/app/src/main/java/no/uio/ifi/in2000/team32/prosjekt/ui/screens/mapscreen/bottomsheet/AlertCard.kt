package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.AlertList

@Composable
fun AlertCard(alertList: AlertList, navController: NavController) {
    val alert = alertList.listOfAlerts.first()

    val alertEvent = alert.event.lowercase()
    val alertColor = alert.riskColor.lowercase()
    val iconFileName = "icon-warning-$alertEvent-$alertColor.png"
    val iconPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("file:///android_asset/alertSymbols/128/$iconFileName").build()
    )

    val alertContainerColor = colorFromString(alertColor)
    val alertTextColor = if (alertColor == "yellow") Color.Black else Color.White

    Card(
        onClick = { navController.navigate("alertScreen/${alertList.lat}/${alertList.lon}") },
        colors = CardDefaults.cardColors(containerColor = alertContainerColor),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .width(380.dp)
            .padding(16.dp)
    ) {

        Column(Modifier.padding(8.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (alertList.listOfAlerts.size > 1) {
                    ImageWithBadge(
                        image = iconPainter,
                        badgeContent = {
                            Badge(content = {
                                Text(
                                    text = "${alertList.listOfAlerts.size}+", color = Color.White
                                )
                            })
                        },
                    )
                } else {
                    Image(
                        painter = iconPainter,
                        contentDescription = "Weather Icon",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = alert.eventAwarenessName,
                        color = alertTextColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = alert.area,
                        color = alertTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

        }

        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(), horizontalAlignment =  Alignment.CenterHorizontally) {
            Text(text = "Trykk her for mer informasjon",
                fontStyle = FontStyle.Italic,
                textDecoration = TextDecoration.Underline,
                color = Color.Blue
            )
        }
    }
}

fun colorFromString(stringColor: String): Color {
    val metAlertsRed = "#C60000"
    val metAlertsOrange = "#FF9D00"
    val metAlertsYellow = "#FFE600"

    val hexColor = when (stringColor.lowercase()) {
        "red" -> metAlertsRed // alternative red color: "#E62F2D"
        "orange" -> metAlertsOrange
        "yellow" -> metAlertsYellow
        else -> "#5671A6"
    }
    return Color(android.graphics.Color.parseColor(hexColor))
}


@Composable
fun ImageWithBadge(
    image: Painter,
    badgeContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    badgeSize: Dp = 36.dp,
) {
    Box(modifier = modifier) {
        Image(
            painter = image, contentDescription = "Weather icon",
            modifier = Modifier.padding(8.dp)
        )

        Box(
            modifier = Modifier
                .size(badgeSize)
                .offset(x = 28.dp, y = 0.dp), contentAlignment = Alignment.Center
        ) {
            badgeContent()
        }
    }
}







