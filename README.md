# Havsus - ReadMe
Havsus er en app for å vise badesteder rundt om i Norge med tilhørende værvarsel for de forskjellige badestedene. I tillegg til dette inneholder Havsus også funksjonalitet for å hente værinformasjon for andre steder på og ved sjøen i Norge slik at dersom du planlegger å oppholde deg utenfor de viste badestedene, for eksempel om du er ute på sjøen i båt eller kajakk, vil du også kunne se værforholdene her.

# Innholdsfortegnelse
1. Installasjonsguide
2. Dokumentasjon
3. Biblioteker og rammeverk

<br>
<br>

# 1. Installasjonsguide

Installasjonsguiden viser hvordan applikasjonen lastes ned og kjøres i Android Studio. Det forutsettes at man har en emulator med minimum API-level 24.
<br>
<br>
<br>
<b>Kommando for å laste ned prosjektet:</b>


```console
$ git clone  https://github.uio.no/IN2000-V24/team-32.git
```

<br>
<br>


## 1.1 Kjøre appen i Android Studio sin emulator
Nå er du (forhåpentligvis) klar til å kjøre appen i en emulator i Android Studio.

Trykk <b>Build > Rebuild Project</b> fra menyen øverst:

![Screenshot 2024-05-13 at 18 31 49](https://media.github.uio.no/user/9349/files/6a04ebea-e3ca-4e8a-800b-a0c79989daf5)


Deretter kan applikasjonen kjøres ved å trykke <b>Run > Run 'app'</b> fra menyen øverst:

![Screenshot 2024-05-13 at 18 33 42](https://media.github.uio.no/user/9349/files/66a36a57-45c9-48cd-a2c8-7db733a1cc87)

### NB! Hvis Android Studio sier "SDK location not found" eller den ikke klarer å finne en fil 'local.properties', sørg for å klikke på "Sync Project with Gradle Files" først. Denne knappen kan du finne under 'File' i 'Main Menu', øverst i venstre hjørne.

## 1.2 Sette lokasjon i Android Studios emulator
For å utnytte all funksjonalitet i applikasjonen trenger man å gi enheten tilgang til dens posisjon. 

Om du kjører applikasjonen på en emulator i Android Studio er det nødvendig å sette denne posisjonen manuelt. Det gjøres slik:

0. Hold musepekeren over menyen i emulatorvinduet og trykk "Extended controls"-knappen som er helt til høyre:
![Screenshot 2024-05-16 at 13 46 46](https://media.github.uio.no/user/9349/files/8eabc8c1-075e-4643-aacd-b60451597f58)

1. Da åpnes Extended controls-vinduet. Her velger du et sted i søkefeltet til kartet som inkluderes i dette vinduet (punkt 1. i bildet under).

2. Når du har valgt sted trykker du "Save point"-knappen nederst (punkt 2. i bildet under).

3. Et nytt vindu dukker opp, her velger du eventuelt et navn på stedet du har valgt. Deretter trykker du "ok" for å lage stedet og lukke vinduet.

4. Til slutt sørger du for at det nye stedet er valgt (punkt 3. i bildet under), og trykker "set location" (punkt 4. i bildet under).

6. Du har nå satt en posisjon til emulatoren, og kan lukke Extended Controls-vinduet og benytte appen som du ønsker!
![Screenshot 2024-05-16 at 13 54 00](https://media.github.uio.no/user/9349/files/1d7f26aa-34ae-4240-a1da-6c5dc63ade64)



# 2. Dokumentasjon

## 2.1 Plattformer applikasjonen kjører på
Applikasjonen krever minimum Android API-level 26 (tilsvarer Android versjon 8.0 Oreo) eller høyere. Nærmere beskrivelse av godkjente plattformer applikasjonen kan kjøres på finner du i [ARCHITECTURE.md](ARCHITECTURE.md).

## 2.2 Hvordan bruke Havsus
Beskrivelse av applikasjonens funksjonalitet og hvordan man interagerer med den på en hensiktmessig måte finner du i prosjektrapportens kapittel 2.2.

## 2.3 Arkitektur
En oversikt over løsningen vår og en mer detaljert beskrivelse av applikasjonens arkitektur ligger i [ARCHITECTURE.md](ARCHITECTURE.md)

## 2.4 Diagrammer og modellering
Visualisering av de viktigste funksjonalitetene i applikasjonen ligger i [MODELLING.md](MODELLING.md)

# 3. Biblioteker og rammeverk
<table border="1">
    <tr>
        <th>Rammeverk</th>
        <th>Beskrivelse</th>
    </tr>
    <tr>
        <td>Ktor</td>
        <td>Brukes til å lese inn data fra API-er i JSON-format.<br>https://ktor.io/docs/welcome.html</td>
    </tr>
    <tr>
        <td>Mapbox</td>
        <td>Maps SDK for Android for kartvisning.<br>https://docs.mapbox.com/android/maps/guides/</td>
    </tr>
    <tr>
        <td>JUnit5</td>
        <td>Rammeverk for testing.<br>https://junit.org/junit5/</td>
    </tr>
    <tr>
        <td>MockK</td>
        <td>Rammeverk for å genereres "mockdata" til enhetstestng av datasource og repository-filer.<br>https://mockk.io/</td>
    </tr>
    <tr>
        <td>MPAndroidChart</td>
        <td>Eksternt rammeverk brukt for å tegne grafer.<br>https://github.com/PhilJay/MPAndroidChart</td>
    </tr>
    <tr>
        <td>Coil</td>
        <td>Brukes for bildehåndtering for å vise farevarsler og værikoner.<br>https://coil-kt.github.io/coil/compose/</td>
    </tr>
    <tr>
        <td>Google Location Services</td>
        <td>Brukes til å hente brukerens nåværende plassering og tilby lokasjonsbaserte tjenester.</td>
    </tr>
    <tr>
        <td>Værikoner fra YR og NRK</td>
        <td>Brukes til å vise en ikonisk, visuell oppsummering av værforhold.<br>https://nrkno.github.io/yr-weather-symbols/<br></td>
    </tr>
    <tr>
        <td>Farevarslingsikoner fra YR og NRK</td>
        <td>Brukes til å vise en ikonisk, visuell av farevarsel.<br>https://nrkno.github.io/yr-warning-icons/</td>
    </tr>
</table>


<br>
<br>


# 4 Kildehenvisning for værikoner og fareikoner


<b>NRK. (u å).</b> <i>Yr weather symbols.</i><br>
     Hentet April 2024 fra NRK - GitHub:<br>
     https://nrkno.github.io/yr-weather-symbols/<br><br>
    
<b>NRK. (u å).</b> <i>Yr Warning Icons.</i><br>
     Hentet April 2024 fra NRK - GitHub:<br>
     https://nrkno.github.io/yr-warning-icons/<br><br>
