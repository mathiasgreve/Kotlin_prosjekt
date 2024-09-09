# Arkitekturoversikt - HavSus

Dette dokumentet gir en detaljert oversikt over arkitekturen til Havsus-appen. Denne teksten er ment for utviklere, driftsteam og alle som er involvert i den løpende utviklingen og driften av applikasjonen.

<br><br>

## Innholdsfortegnelse

<table>
    <tr>
        <td>1</td>
        <td>Arkitektonisk Stil</td>
    </tr>
    <tr>
        <td>2</td>
        <td>Implementering av objektorienterte prinsipper</td>
    </tr>
    <tr>
        <td>3</td>
        <td>Teknologier og rammeverk</td>
    </tr>
    <tr>
        <td>4</td>
        <td>API-nivå og Plattformkrav</td>
    </tr>
    <tr>
        <td>5</td>
        <td>Kildehenvisning</td>
    </tr>
</table>

<br><br>

## 1) Arkitektonisk Stil
Appens arkitektur er bygget etter arkitekturmønstret MVVM (Model-View-ViewModel), som er den anbefalte arkitekturen for android apper. Arkitekturen deler også ansvarsområder inn i fire distinkte lag, der data-laget og dommenelaget utgjør Modellen, mens både View og ViewModel sammen representerer UI-laget. 

For å ha et enda tydeligere skille mellom businesslogikk og annen logikk, organsieres programmets use cases-klasser i et domenelag. Dette valget ble tatt på bakgrunn av behovet for å kombinere informasjon hentet fra flere datakilder knyttet til ett use-case. Ved å ha denne strukturen, er tanken at arkitekturen legger til rette potensiel fremtidig utvidelse av prosjektet med tanke på funksjonalitet.

### Oversikt over appens arkitektur

<body>
    <table>
        <thead>
            <tr>
                <th>Komponent</th>
                <th>Inneholder</th>
                <th>Beskrivelse</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Modell</td>
                <td>
                    <ul>
                        <li>DataSourceklasser</li>
                        <li>Repositoryklasser</li>
                        <li>UseCase-klasser</li>
                        <li>Dataklassen knyttet til innsamling av data</li>
                    </ul>
                </td>
                <td>Alle komponenter som ligger i pakkene data, modell eller domain er en del av modellen</td>
            </tr>
            <tr>
                <td>ViewModel</td>
                <td>
                    <ul>
                        <li>MapViewModel</li>
                        <li>AlertViewModel</li>
                    </ul>
                </td>
                <td>Alle klassene med ViewModel-prefiks som ligger i screen-pakkene.</td>
            </tr>
            <tr>
                <td>View</td>
                <td>
                    <ul>
                        <li>Alle Composables-metoder</li></td>
                    </ul>
                <td>Alle metodene med screen-suffiks i screen-pakkene</td>
            </tr>
        </tbody>
    </table>
</body>
</html>


### Forenklet arkitekturtegning over appen


![Havsus - arkitekturoverikt](https://media.github.uio.no/user/8418/files/e6d40645-d3f6-4fe4-ac82-3921713fef30)

Trukk på denne linken for å se en mer utvidet versjon av arkitekturtegningen
[Utvidet arkitekturtegning](https://github.uio.no/IN2000-V24/team-32/blob/main/bilder_modellering/Havsus%20-%20Utvidet%20arkitekturtegning.png)


<br><br>

## 2) Implementering av objektorienterte prinsipper og bruk av designmønstre

### Koblingsgrad

I arbeidet med å utvikle «Havsus» har vi tilstrebet å oppnå målet om «Lav kobling» og «Høy Kohesjon»(Lindsjørn, 2024) så godt det har latt seg gjøre. For å oppnå dette har vi gjort følgende tiltak:

<b>Separasjon av datakilder</b>:
Ved å bruke én fil datasource-klass per datakilde, separeres mekanikken for innlesing av data på en slik måte at datainnlesingen fra hvert API kan gjøres uten å påvirke resten av systemet.

<b>Modularisering</b>
For å forsøke å nå idealet om «lav kobling», har vi vært opptatt av å designe komponentene i koden slik at de er så uavhengige av hverandre som mulig. Ved å ha høy grad av modulisering av metoder, composables og klasser, har vi forsøkt å utvikle programmet slik at endringer i én del av systemet har minst mulig innvirkning på andre komponenter i applikasjonen.
Et av prinsippene vi har jobbet etter for å nå dette målet, er konseptet om «å unngå store klasser og lange metoder», da dette bidrar til å tydelig fokusere og avgrense hvert objekts ansvarsområde. 

<b>Bruk av arv for å unngå duplikat av kode</b>
Vi har bevisst brukt abstrakte klasser som grep for å redusere duplikat av kode. Et eksempel på dette er hvordan vi lar datasource-klassenen våre arve HTTP-klienten fra den abstrakte klassen «DataSource», mens et annet eksempel er hvordan vi samler presentasjonslogikken for brukerens GPS-posisjon i «UserLocationViewModel» gjøre det lett å gjennbruke denne funksjonaliteten for andre ViewModeller. I og med at "maintainability" er en kvalitetsegenskap vi tilstreber, vil bruke denne bruken av abstaksjon gjøre dette det enklere å utvide med nye skjermer og ViewModeller i framtiden.


<b>Bruk av MVVM-arkitektur</b>
Et positiv konsekvens av at programmet benytter en MVVM-arkitektur, er at den bidrar til å avgrense og fokusere hver systemkomponents ansvarsområde – noe som også medvirker til å skape høy kohesjon i systemet.


### UDF (Unidirectional Data Flow)
Unidirectional Data Flow sikrer at data beveger seg i en enkelt retning gjennom applikasjonen, noe som gjør systemet mer forutsigbart og lettere å feilsøke.

Her er noe vi har skrevet om hvordan vi jobber for å oppnå design-mønstret om Unidirectional Data Flow (UDF)

<u>Dette er tiltak vi har gjort for å etterstrebe idealet om Unidirectional Data Flow</u>

<b>State hoisting</b>
Ved å «heise» tilstandsvariabler opp til laveste felles foreldre i komponenthierarkiet, forsøker vi i den grad mulig å oppnå en UDF-flyt i appliksjonen.
Dette fører at alle underkomponenter kan dele den samme tilstanden, uten behov for å sende den nedover i komponenttreet. Gjennom å organsere koden hieriskik i lagdeling også i UI-laget, medvirkes det til at "dataflyten" (eller eventflyten) i presentasjons-laget går oppover i kommunikasjonen mellom komponentene - noe som er i henhold til designmøstret om UDF. Dessuten bidrar denne sentralisere av tilstandsvariablene til å fokusere kodebasen mot kvallitetsegenskapen <i>maintainability</i> da dette bidrar til å forenkle vedlikehold og utvidelse av koden (Wang & Almås, 2024 a, s. 68).

 
<b>Repository som datakontroller</b> 
Å organisere datalaget etter en datasource-repository-strukur, slik at repositoryklassene sin rolle er å transformere og videreformidle de manipulerte dataene, sørger dette for at dataflyten går én vei i systemet (Wang & Almås, 2024 b, s. 38).


### Oppsummering av appens koblingsgrad og kohesjonsgrad: Designvalg og appens natur påtvinger noe høy kobling i UI-laget
Når det gjelder presentasjonslaget, pålegger appens UI-design den tekniske løsningen å samle mye funksjonallitet på én skjerm. Ettersom programmets kjernefunksjonalitet er sentret rundt én skjerm – altså «MapScreen», medfører dette at skjermens tilhørende «MapViewModel» er avhengig av hele fem andre objekter. På bakgrunn av den høye avhengighetsgraden til denne ViewModel-klassen, har vi derfor forsøkt å oppnå «lav kobling» i andre deler av appen. Behovet for å få den totale koblingsgraden i applikasjonen ned, var en sterkt medvirkende årsak til at vi flyttet noe businesslogikk til dommenelaget og ellers har prioritert å ha få avhengigheter mellom komponentene i data-laget. Altså blir systemets koblingsgrad noe høy grunnet designvalget om å samle mye funksjonalitet på ett sted, men vi har etter beste evne forsøkt å kompensere for dette andre steder i appen. Samtidig har appens høye grad av modularisering og tydelige dataflyt også høy kohesjon.


<br><br>



## 3) Teknologier og rammeverk

<table border="1">
    <tr>
        <th>Rammeverk</th>
        <th>Beskrivelse</th>
    </tr>
    <tr>
        <td>Ktor</td>
        <td>Brukes til å lese inn data fra API-er i JSON-format.</td>
    </tr>
    <tr>
        <td>Mapbox</td>
        <td>Brukt som leverandør av tredjeparts kartløsning.</td>
    </tr>
    <tr>
        <td>JUnit5</td>
        <td>Brukt for enhetstesting av komponenter.</td>
    </tr>
    <tr>
        <td>MockK</td>
        <td>Brukt for å genere «mockdata» til enhetstester av repositoryklasser.</td>
    </tr>
    <tr>
        <td>MPAndroidChart</td>
        <td>Eksternt rammeverk brukt for å tegne grafer.</td>
    </tr>
    <tr>
        <td>Coil</td>
        <td>Brukes for bildehåndtering for å vise farevarsler og værikoner.</td>
    </tr>
    <tr>
        <td>Google Location Services</td>
        <td>Brukes til å hente brukerens nåværende plassering og tilby lokasjonsbaserte tjenester.</td>
    </tr>
    <tr>
        <td>Værikoner fra YR og NRK</td>
        <td>Brukes til å vise en ikonisk, visuell oppsummering av værforhold.</td>
    </tr>
    <tr>
        <td>Farevarslingsikoner fra YR og NRK</td>
        <td>Brukes til å vise en ikonisk, visuell av farevarsel.</td>
    </tr>
</table>


<br>
<br>

## 4) API-nivå og plattformkrav

### Havsus trenger både minimum API-nivå 26 (Android 8.0 Oreo) og OpenGL ES 3.0, for å kunne kjøres
Dette valget er motivert av følgende faktorer:

<b>Tilgjengelighet av funksjoner:</b> Vi ønsker å ha tilgang til så ny og god OS-funksjonalitet som mulig. Da er det nødvendig med et tilstrekkelig høyt API-nivå for å oppnå dette. Årsaken til kravet om OpenGL ES 3.0, er at appen tar i bruk Mapbox, som krever dette for sin kjernefunksjonalitet.

<b>Tilgjengelighet i markedet:</b> Samtidig ønsker vi også at appen skal være tilgjengelig for en bred brukermasse. Dette krever da et API-nivå som ikke er satt for høyt, slik at appen er kompatibel med eldre enheter som ikke nødvendigvis støtter de aller nyeste API-nivåene.

<b>Kompabilitet med tredjepartsbiblioteker:</b> Havsus er i stor grad en kart-app, og vi er derfor avhengige av Mapbox SDK for Android. Dette tredjeparts biblioteket krever API-nivå 21 eller høyere, som legger føringer for hvor langt ned vi kan gå i API-level.


<b>For mer informasjon om dette se disse linkene:</b>
<table>
    <tr>
        <th>Ressurs</th>
        <th>Link</th>
    </tr>
    <tr>
        <td>Maps SDK for Android</td>
        <td><a href="https://docs.mapbox.com/android/maps/guides/">https://docs.mapbox.com/android/maps/guides/</a></td>
    </tr>
    <tr>
        <td>Mapbox Compose Extension (version 11.2.0)</td>
        <td><a href="https://github.com/mapbox/mapbox-maps-android/blob/main/extension-compose/README.md">https://github.com/mapbox/mapbox-maps-android/blob/main/extension-compose/README.md</a></td>
    </tr>
</table>


<b>Redgjørelse for valg av API-nivå</b>
På bakgrunn av disse faktorene nevnte over har vi derfor landet på at API-nivå 26 (Android 8.0 Oreo) er det optimale nivået for vår app. Da oppnår vi en relativt moderne app med tilstrekkelig funksjonalitet fra de høyere API-nivåene, som samtidig når ut til mange brukere da den er kompatibel med mange enheter (93.7% i henhold til informasjon fra Android Studio. Se bilde under), i tillegg til å oppfylle minimumskravene som pålegges av våre tredjepartsbiblioteker.

I forbindelse med bestemmelsen av API-nivå testet vi applikasjonen på ulike API-nivåer for å forsikre oss om at applikasjonen fungerer optimalt på alle API-nivåene den er ment å kunne fungere på.  

<br>

<b>Oversikt over kompabilitet for ulike OS og API-nivåer</b>

![Screenshot 2024-05-16 at 10 47 37](https://media.github.uio.no/user/9349/files/3771a39e-165e-4e57-af50-7dfb2c58704d)
Dette bildet, med statistikk over API-nivåene, genereres av Android Studio ved opprettelse av ny emulator.

<br>

## 5) Datakilder brukt i applikasjonen

I tabellen under vises en oversikt over hvilke API-er programmet bruker, og hvilke data den finner fra hvert enkelt API.


<table border="1">
  <tr>
    <th>API</th>
    <th>Data</th>
  </tr>
  <tr>
    <td>MetAlerts</td>
    <td>
      <ul>
        <li>area</li>
        <li>awarenessResponse</li>
        <li>awarenessSeriousness</li>
        <li>awareness_level</li>
        <li>awareness_type</li>
        <li>consequences</li>
        <li>contact</li>
        <li>description</li>
        <li>event</li>
        <li>eventAwarenessName</li>
        <li>instruction</li>
        <li>resources</li>
        <li>severity</li>
        <li>web</li>
        <li>riskMatrixColor</li>
        <li>interval</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>Location-Forecast</td>
    <td>
      <ul>
        <li>Air_temperature</li>
        <li>Wind_speed</li>
        <li>Wind_from_direction</li>
        <li>Ultraviolet_index_clear_sky</li>
        <li>symbol_code (next_1_hours.summary)</li>
        <li>Precipitation_amount (next_1_hours.details)</li>
        <li>Time (properties.timeseries)</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>FrostHavvarsel</td>
    <td>
      <ul>
        <li>Lat</li>
        <li>Lon</li>
        <li>Details?</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>OceanForecast</td>
    <td>
      <ul>
        <li>Vanntemperatur</li>
        <li>bølgehøyde</li>
        <li>strømstyrke.</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>MapBox SDK</td>
    <td>
      <ul>
        <li>Kartvisning</li>
        <li>Søkefunksjonalitet</li>
        <li>Lokasjonsforslag</li>
      </ul>
    </td>
  </tr>
</table>


<b>For mer informasjon se:</b>

<ul>
  <li><a href="https://api.met.no/weatherapi/metalerts/2.0/documentation">MetAlerts</a></li>
  <li><a href="https://api.met.no/weatherapi/locationforecast/2.0/documentation">Location-Forecast</a></li>
  <li><a href="https://havvarsel-frost.met.no/">HavvarselFrost</a></li>
  <li><a href="https://api.met.no/weatherapi/oceanforecast/2.0/documentation">OceanForecast</a></li>
  <li><a href="https://docs.mapbox.com/android/maps/guides/">MapBox SDK</a></li>
</ul>

<br>
<br>


# 5) Kildehenvisning

<b>Lindsjørn, Y. (2024, Februar 9)</b>. <i>Kravhåndtering, modellering, design og prinsipper.</i><br>
      Hentet fra IN2000 Software Engineering med prosjektarbeid:<br>
      https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/forelesninger/in2000.2024.02.08.krav_design_modellering.pdf<br><br>

<b>NRK. (u å).</b> <i>Yr weather symbols.</i><br>
     Hentet April 2024 fra NRK - GitHub:<br>
     https://nrkno.github.io/yr-weather-symbols/<br><br>
    
<b>NRK. (u å).</b> <i>Yr Warning Icons.</i><br>
     Hentet April 2024 fra NRK - GitHub:<br>
     https://nrkno.github.io/yr-warning-icons/<br><br>


<b>Wang, S. B., & Almås, S. (2024, Januar 19).</b> <i>Vi ser videre på Android og Kotlin.</i><br> 
      Hentet fra IN2000 Software Engineering med prosjektarbeid:<br>
      https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/timeplan/2.-videre-android-og-kotlin.pdf<br><br>

<b>Wang, S. B., & Almås, S. (2024, Januar 23).</b> <i>Tilstand, Coroutines og intro til app-arkitektur</i>.<br>
      Hentet fra IN2000 Software Engineering med prosjektarbeid:<br>
      https://www.uio.no/studier/emner/matnat/ifi/IN2000/v24/forelesninger/3.-compose_-tilstand-intro-til-app-arkitektur-og-coroutines.pdf




