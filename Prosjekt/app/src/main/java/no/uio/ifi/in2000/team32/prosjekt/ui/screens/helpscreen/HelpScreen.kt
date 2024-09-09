package no.uio.ifi.in2000.team32.prosjekt.ui.screens.helpscreen

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import no.uio.ifi.in2000.team32.prosjekt.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    navController: NavHostController,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bruksanvisning") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
            innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surfaceBright),
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            item {
                Text(
                    text = buildAnnotatedString {
                        append("Om ")
                        withStyle(
                            SpanStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Cyan, Color.Blue)
                                )
                            )
                        ) {
                            append("Havsus")
                        }},
                    modifier = Modifier
                        .width(530.dp)
                        .padding(bottom = 12.dp, top = 12.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                )


                Text(
                    text = intro,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )

                Text(
                    text = intro2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )

                Text(
                    text = part1,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 48.dp),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )

                Text(
                    text = part1_1,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                )

                Text(
                    text = part1_1_text,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )


                Text(
                    text = part2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 48.dp),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = part2_text,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = part2_1,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                )

                Text(
                    text = part2_1_text,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = part2_2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                )

                TextWithDrawablePic(
                    drawableId = R.drawable.lokasjonillu,
                    modifier = Modifier.fillMaxWidth(),
                    imageSize = 280.dp,
                    contentDescription = "Picture. Find user location button is located in the lower right in the screen."
                )

                Text(
                    text = part2_2_text1,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = part2_2_text2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                val part22Text3 = AnnotatedString.Builder().apply {
                    append("Brukerlokasjonen indikeres i kartet med symbolet")
                    appendInlineContent("drawable")
                }.toAnnotatedString()

                TextWithDrawableSymbol(
                    text = part22Text3,
                    drawableSymbol = R.drawable.userpoint,
                    contentDescription = "User location icon",
                    iconSize = 28.sp
                )


                Text(
                    text = part2_3,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                )

                TextWithDrawablePic(
                    drawableId = R.drawable.finnbadillu,
                    modifier = Modifier.fillMaxWidth(),
                    imageSize = 280.dp,
                    contentDescription = "Picture. Pushing the button finn badeplasser in the lower right in the screen will display swimming spots in the map"
                )

                Text(
                    text = part2_3_text2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )


                val del23Tekst1 = AnnotatedString.Builder().apply {
                    append("Badeplassene indikeres i kartet med symbolet")
                    appendInlineContent("drawable")
                }.toAnnotatedString()

                TextWithDrawableSymbol(
                    text = del23Tekst1,
                    drawableSymbol = R.mipmap.badested,
                    contentDescription = "Swimming spot icon",
                    iconSize = 32.sp,
                )


                Text(
                    text = part4,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 48.dp),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = part4_1,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                )

                TextWithDrawablePic(
                    drawableId = R.drawable.velgbadeinfo,
                    modifier = Modifier.fillMaxWidth(),
                    imageSize = 280.dp,
                    contentDescription = "Picture showing how to get weather information for a swimming spot"
                )

                Text(
                    text = part4_1_text,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = part4_1_text2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp, bottom = 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                TextWithDrawablePic(
                    drawableId = R.drawable.farevarselinfo,
                    modifier = Modifier.fillMaxWidth(),
                    imageSize = 280.dp,
                    contentDescription = "Picture showing how weather information is displayed"
                )

                Text(
                    text = part4_1_text3,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )


                Text(
                    text = part4_2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                )


                Text(
                    text = part4_2_text,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                val del4symbol = AnnotatedString.Builder().apply {
                    append("Dette stedet indikeres med et pin-symbol i kartet:")
                    appendInlineContent("drawable")
                }.toAnnotatedString()

                TextWithDrawableSymbol(
                    text = del4symbol,
                    drawableSymbol = R.mipmap.pin_trans_2,
                    contentDescription = "Pin icon",
                    iconSize = 32.sp,
                )


                Text(
                    text = del4_2_tekst2,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
//        Denne appen gir deg informasjon om badeplasser nær deg og lar deg utforske
//        værinformasjon for hvert sted. Her er en liten guide til hvordan du bruker de ulike funksjonene i appen.
val intro = """
Havsus gir deg tilgang til detaljert og oppdatert værinformasjon og farevarslinger for lokasjoner ved kysten i Norge. Appen kan brukes for å sjekke værforholdene og farevarsler i sanntid, slik at det blir enklere for deg å planlegge aktiviteter du har lyst til å gjøre ved kysten. Vi ser for oss at appen bidrar til å gjøre ferdsel ved kysten tryggere ved at brukerne enkelt får tilgang til den informasjonen de trenger.
""".trimIndent()

val intro2 = """Her er en oversikt over appens funksjonalitet og fungerer som en guide for hvordan du bruker appen:""".trimIndent()

val part1 = """
        Forhåndsbetingelser og tillatelser
""".trimIndent()

val part1_1 = """
        Tilgang til posisjon
""".trimIndent()

val part1_1_text = """
    Enkelte funksjoner i applikasjonen krever tilgang til enhetens lokasjon og av hensyn til personvernet ditt vil applikasjonen derfor spørre deg om du vil tillatelse til å dele posisjonen din. Ved oppstart vises derfor et vindu med mulighet for å velge å gi tillatelse til å dele lokasjon. Du kan velge mellom 1) å dele lokasjon én gang, 2) dele lokasjon når appen er i bruk eller 3) avslå muligheten til å dele lokasjon. I tillegg til dette kan du velge hvor nøyaktig posisjonen som deles skal være. Du har muligheten til å å dele enten presis lokasjon eller grov lokasjon.
""".trimIndent()

val part2 = """
    Generelt
""".trimIndent()

val part2_text = """
Kartet viser i utgangspunktet hele verden, men applikasjonen er kun garantert å gi presis informasjon om været innenfor Norges landegrenser. Det er derfor visualisert i kartet et skille mellom Norge og andre landområder, der norske landområder vises tydeligere, for å indikere denne distinksjonen for hvor det er ment å hente værinformasjon fra. """.trimIndent()

val part2_1 = """
    Søkefunksjon
""".trimIndent()

val part2_1_text = """
For å bruke søkefunksjonen skriver du inn et stedsnavn i søkefeltet nederst på skjermen og deretter velge et av forslagene som dukker opp. Det er kun mulig å søke på steder i Norge. 

Ved vellykket søk vil kartet forflytte seg til det valgte stedet, og automatisk hente badeplasser innenfor en radius på 20 km. """.trimIndent()



val part2_2 = """
    Gå til brukerlokasjon
""".trimIndent()

val part2_2_text1 = """
Nede til høyre i skjermen er det en brukerlokasjon-knapp (se bilde over). Dersom du har gitt applikasjonen tilgang til å benytte enhetens lokasjon vil du ved å trykke på denne knappen forflytte kartet til enhetens lokasjon og automatisk hente badeplasser innen radius på 20 km fra der du befinner deg.""".trimIndent()

val part2_2_text2 = """
Det er indikert i denne knappen hvorvidt det applikasjonen har tilgang på brukerlokasjon eller ikke. Når knappen er delvis gjennomsiktig, har applikasjonen ikke tilgang på brukerlokasjonen din. """.trimIndent()

//val del2_2_tekst3 = """
//Brukerlokasjonen indikeres med symbolet (SYMBOL) i kartet""".trimIndent()

val part2_3 = """
    Finn badesteder
""".trimIndent()

val part2_3_text2 = """
    For å vise badesteder i kartet benyttes knappen “Finn badeplasser” plassert nederst til høyre i skjermen, under brukerlokasjons-knappen. 
    
    Når denne trykkes, vil det vises badeplasser innenfor en radius på 20 km fra midten av kartet vises. Dersom ingen badeplasser vises indikerer det at det ikke er noen registrerte badeplasser med måleinstrumenter for måling av værinformasjon i området av kartet brukeren forsøkte å hente badeplasser for. 
""".trimIndent()


val part4 = """
    Værvarsler
""".trimIndent()

val part4_1 = """
    Værvarsel badested 
""".trimIndent()


val part4_1_text = """
    For å få værvarsel for et badeplass, trykkes badeplass-symbolet.
      
""".trimIndent()

val part4_1_text2 = """
    Da dukker det så opp et vindu med værinformasjon for de neste 24 timene fra bunnen av skjermen. Denne kan gjøres større ved å trekkes oppover, eller lukkes ved å trekke den ned. 

    For å se en graf-visning av værvarselet blar du til venstre i værinformasjonsvinduet. 
""".trimIndent()

val part4_1_text3 =
    """
    
    Dersom det har blitt utstedt et farevarsel for området i nærheten av den valgte badeplassen vil det være en synlig farevarsels-boks øverst i værvarselvinduet. Bakgrunnsfargen på denne boksen indikerer farenivået, rødt indikerer at det er sterkt anbefalt å ikke ferdes i området, mens gult indikerer at man bør være varsom. Et eksempel på dette vises i bildet over. Dersom det er flere farevarsler pågående samtidig indikeres dette med et tall for det totale antallet farevarsler og er plassert over farevarsel-ikonet til venstre i denne boksen. 

    Denne varselboksen kan trykkes på, og du blir da presentert med et større vindu som kun er dedikert til farevarsler. Dersom det er flere farevarsler for samme sted vil alle de ulike varslene vises her, og hver og en kan trykkes på for mer detaljert informasjon om det valgte farevarselet. 
""".trimIndent()

val part4_2 = """
    Få værvarsel for valgt sted
""".trimIndent()

val part4_2_text = """
    På samme måte som du kan se værvarslet for en badeplass er det også mulig å hente værvarsel for et valgt sted på kartet som er på, eller i nærheten av, sjøen. 
    
    Du må da trykke og holde inne på et sted i kartet. Da vil kartet sentreres på denne posisjonen og et vindu med værvarsel vil dukke opp fra bunnen av skjermen.  
""".trimIndent()

val del4_2_tekst2 = """
    Visning av værinformasjon og farevarsel er det samme som for badeplass. Dersom du forsøker å hente værinformasjon for steder der værinformasjon ikke er tilgjengelig, for eksempel for et sted som ikke er langt utenfor Norges landegrenser, vil værinformasjonen vises slik “--:--”. Det samme gjelder for steder som ikke er i nærheten av sjøen. Ved forsøk på å hente værinformasjon fra steder på land, men som allikevel er tilstrekkelig nærme sjøen, vil værinformasjonen som vises være hentet fra området i havet nærmest stedet en valgte. 
""".trimIndent()


@Preview
@Composable
fun PreviewHelpScreen() {
    val navController = rememberNavController()
    HelpScreen(navController)
}

//Innholdet i denne funksjonen er generert av ChatGpt
@Composable
fun TextWithDrawablePic(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    imageSize: Dp = 24.dp,
    contentDescription: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = drawableId).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            ),
            contentDescription = contentDescription,
            modifier = Modifier.size(imageSize)
        )
    }
}


//Innholdet i denne funksjonen er generert av ChatGpt
@Composable
fun TextWithDrawableSymbol(
    text: AnnotatedString,
    drawableSymbol: Int,
    contentDescription: String,
    iconSize: TextUnit,
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(horizontal = 28.dp)
            .padding(top = 6.dp),
        style = MaterialTheme.typography.bodyMedium,
        inlineContent = mapOf(
            "drawable" to InlineTextContent(
                Placeholder(
                    width = iconSize,
                    height = iconSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(
                            LocalContext.current
                        ).data(data = drawableSymbol).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                    ),
                    contentDescription = contentDescription
                )
            }
        )
    )
}
