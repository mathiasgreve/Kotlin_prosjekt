package no.uio.ifi.in2000.team32.prosjekt.ui.screens.alertscreen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import no.uio.ifi.in2000.team32.prosjekt.R
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Alert
import no.uio.ifi.in2000.team32.prosjekt.model.swimmingspot.Spot
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.bottomsheet.colorFromString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(
    lat: String?, lon: String?, navController: NavHostController, alertViewModel: AlertViewModel
) {
    val alertsResource = alertViewModel.alerts.collectAsState()

    if (lat != null && lon != null) {
        alertViewModel.updateAlerts(Spot(lat, lon))
        Log.w("AlertScreen", "lat og lon endret")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Farevarsler") }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")

                }
            })
        }
    ) { innerPadding ->
        when (val alerts = alertsResource.value) {
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is Resource.Success -> {
                val alertList: List<Alert> = alerts.data.listOfAlerts
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)  // Apply the padding here
                ) {
                    items(alertList) { alert ->
                        ExpandableCard(
                            alert
                        ) // Padding for each card
                    }
                }
            }

            is Resource.Error -> {
                ErrorContent(innerPadding)
            }
        }
    }
}

@Composable
fun ErrorContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), // Apply the padding here
        contentAlignment = Alignment.Center // This centers the content within the Box
    ) {
        Text(text = "Farevarsler er dessverre ikke tilgjengelig for øyeblikket")
    }
}


@Composable
fun ExpandableCard(
    alert: Alert
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val alertEvent = alert.event.lowercase()
    val alertLevel = alert.riskColor.lowercase()
    val iconFileName = "icon-warning-$alertEvent-$alertLevel.png"
    val iconPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data("file:///android_asset/alertSymbols/128/$iconFileName").build()
    )
    val alertContainerColor = colorFromString(alertLevel)
    val alertTextColor = if (alertLevel == "yellow") Color.Black else Color.White

    Card(
        onClick = { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = alertContainerColor),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(),
    ) {

        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = iconPainter,
                    contentDescription = "Alert Icon",
                    modifier = Modifier.padding(8.dp).size(60.dp)
                )

                Column(Modifier.weight(1f)) {
                    Text(
                        text = alert.eventAwarenessName,
                        color = alertTextColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = alert.area,
                        color = alertTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                }

                //Ikon som angir om elementet er åpent eller lukket i toppen av høyre hjørne
                Icon(
                    tint = Color.Black,
                    painter = painterResource(id = if (expanded) R.drawable.expanded else R.drawable.collapsed),
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { expanded = !expanded }
                )

            }

            //Ikon på bunn av elementet som peker oppover for å indikere at boksen kan kolapses ved trykk
            if (expanded) {
                ExpandableContent(alert, alertTextColor)
                Icon(
                    tint = Color.Black,
                    painter = painterResource(id = R.drawable.expanded),
                    contentDescription = "Collapse",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(40.dp)
                        .padding(8.dp)
                        .clickable { expanded = !expanded }
                )
            }
        }
    }
}

//Modularisert innhold i @ExpandableCard() i egen composable
@Composable
fun ExpandableContent(alert: Alert, alertTextColor: Color) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Ekstra informasjon som blir synlig når kortet er utvidet
        Text(
            text = "Anbefalinger",
            color = alertTextColor,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = alert.instruction,
            color = alertTextColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Konsekvenser",
            color = alertTextColor,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = alert.consequences,
            color = alertTextColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Beskrivelse",
            color = alertTextColor,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = alert.description,
            color = alertTextColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Område",
            color = alertTextColor,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = alert.area,
            color = alertTextColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Bildevisning av farevarsel
        val imageUri = getImageUriFromResources(alert.resources)
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Map highlighting area of alert",
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun getImageUriFromResources(resources: List<no.uio.ifi.in2000.team32.prosjekt.model.metalerts.Resource>): String? {
    return resources.firstOrNull { it.mimeType == "image/png" }?.uri
}

