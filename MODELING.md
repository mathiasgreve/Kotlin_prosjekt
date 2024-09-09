# Diagrammer - Havsus


## Innholdsfortegnelse
<table>
        <tr>
            <td>1</td>
            <td>Forenklet arkitekturoversikt</td>
        </tr>
        <tr>
            <td>2</td>
            <td>Utvidet arkitekturoversikt: Klasse-diagram</td>
        </tr>
        <tr>
            <td>3</td>
            <td>Use-casediagram</td>
        </tr>
        <tr>
            <td>4</td>
            <td>Sekvensdiagram</td>
        </tr>
</table>


<h2> 1) Forenklet arkitekturoversikt </h2>
Under vises en overordnet oversikt over applikasjonens arkitektur.
Tegningen er ment å illustrere hvordan arkitekturens lagdeling er, og hvordan ansvaret fordeles på de ulike lagene i programmet.

De heltrukne linjene representerer avhengighetene mellom komponentene, men de stiplete linjene illustrerer dataflyten i systemet.
 
Appen følger en MVVM-arkitektur det data-laget og dommenelaget utgjør Modellen, mens ViewModel og View til sammen representerer UI-laget.

![Havsus - arkitekturoverikt](https://media.github.uio.no/user/8418/files/0c321720-e44e-4584-974c-5adcb28f9b41)


For mer detaljert innsikt i klassenes innhold og avhengigheter se klassediagrammet «utvidet arkitekturoversikt» i neste avsnitt.


<h2> 2) Utvidet arkitekturoversikt: Klasse-diagram </h2>

Under ser leseren en omfattende og mer <b>detaljert klassediagram</b> som er ment å gi en mer helhetlig innsikt i hvordan programmet er bygget opp.

Ettersom dette diagrammet er såpas inngående og stort, har vi måttet gjøre noen valg for å ha med nok detaljer til å gi dyp forståelse av systemets sammenheng, samtidig som vi unngår å gjøre det <i>helt</i> uleselig. 

<br>

<h4>Som konsekvens av dette har vi derfor bevisst unnlatt å tegne inn:</h4>
<ul>
    <li>Dataklassene som brukes til innlesing av API-data i JSON-format, i modell-pakken</li>
    <li>Stiplete linjer som viser dataflyten oppover</li>
</ul>




For å avgrense tegningens kompleksitet har vi også kun tegnet inn de mest sentrale @Composable-metodene som representerer de viktigste skjermene og UI-mekanikken i appen.

![Havsus - Utvidet arkitekturtegning](https://media.github.uio.no/user/8418/files/c546ca15-b2cf-4349-a329-fa7e7f8f02a4)


Hvis leser har lyst til å se en enda større versjon av tegningen, er den tilgjengelig i større format her: 
[Utvidet arkitekturtegning](https://github.uio.no/IN2000-V24/team-32/blob/main/bilder_modellering/Havsus%20-%20Utvidet%20arkitekturtegning.png)


<br>
<br>
<h2> 3) Use-casediagram </h2>
<br>


![usecase_diagram](https://media.github.uio.no/user/9349/files/0521a314-81f9-4e0c-9b86-31e6306d807f)

<br>

<h3>Tekstlig beskrivelse av Use-case “Se værvarsel på badeplass”</h3>
<br>
<b>Navn på use case:</b> Se værvarsel på badeplass <br>
<b>Primær-aktør:</b> Havsus-bruker <br>
<b>Sekundær-aktør:</b> API-leverandører <br>
<b>Pre-betingelse 1:</b> Enheten som kjører appen har tilgang til internett <br>
<b>Pre-betingelse 2:</b> Det er ingen feil i API-ene til meteorologisk institutt <br>
<b>Pre-betingelse 3:</b> Brukeren har allerede oppdatert badeplassene i kartet og kartet har funnet, og viser, badeplass(er) i kartet.<br>
<b>Post-betingelser:</b> Bruker får opp informasjon om været på valgt badeplass på skjermen.<br>

 

<h3>Hovedflyt:</h3>

1. Bruker trykker på et badeplass-ikon som vises på skjermen. 
2. Kartet sentreres på den valgte badeplassen. 
3. Systemet henter værinformasjon for det valgte badestedet. 
4. Systemet henter farevarsler for posisjonen til det valgte badestedet. 
5. Systemet viser et vindu med værinformasjon. 
6. Bruker interagerer med informasjonen i vinduet og/eller glidepanelet ved å scrolle nedover eller bla til siden. 
7. Bruker scroller glidepanelet helt ned for å lukke det. 
 
<br>

<b>Alternativ flyt 1:</b> Det blir funnet farevarsel, som inkluderes i værvarslet: <br>
5.1 Systemet viser også farevarsel(er) sammen med øvrig værinformasjon. <br>
5.2 Bruker trykker på farevarselet <br>
5.3 Systemet viser utfyllende informasjon om det valgte farevarselet. <br>
5.4 Bruker navigerer seg tilbake til standard-værvarselet. Returnerer til punkt 6 <br>

<br>
<b>Alternativ flyt 2:</b> Bruker velger å bla til neste side av værvisning: <br>

6.1.1 Bruker blar til siden i informasjonsvinduet <br>
6.1.2 Systemet viser en grafvisualisering av værinformasjonen <br>
6.1.3 Bruker navigerer tilbake til standard-værvarselet. Returnerer til punkt 6 <br>

<br>

<b>Alternativ flyt 3:</b> Bruker velger å se mer informasjon om farevarselet:<br>
6.2.1 Bruker trykker på farevarselet <br>
6.2.2 Systemet viser en liste med farevarsler <br>
6.2.3 Bruker trykker på et farevarsel for detaljert informasjon om varslet <br>
6.2.4 Bruker navigerer tilbake til standard-værvarselet. <br>
<b>Returnerer til punkt 6</b>

<br>
<br>


<br>
<br>
<h2> 4) Klassediagram use case - Se værvarsel for badeplass </h2>
<br>

![klassediagram_usercase_seværBad](https://media.github.uio.no/user/9349/files/70f11996-4be9-4cec-9c40-580df2f63233)



<h2> 5) Aktivitetsdiagram use case - Se værvarsel for badeplass </h2>
Dette aktivitetsdiagrammet er ment å supplere beskrivelsen og andre visualiseringer av use caset “se værvarsel på badeplass”. Den viser flyten av hendelsesforløpet til use caset, og forsøker særlig å tydeliggjøre alternativ flyt ved tilfellet at farevarsler er tilgjengelig. På den andre siden inkluderes ikke alternativ flyt for visning av værinformasjon i graf. Dette for å gjøre diagrammet mer oversiktelig og forståelig for den som leser det. 
<br>
<br>

![aktivd_seVærBad_dark-2](https://media.github.uio.no/user/9349/files/31c245fb-c119-491e-bde7-d1af2f9e23c5)
<br>
<br>

<h2> 6) Sekvensdiagram </h2>
<br>
<h3> 6.1 Use case - finn badeplasser </h3>
<br>

![sekvd_finnBad](https://media.github.uio.no/user/9349/files/14ce4b62-8941-4d5f-85ad-3f4511b0a6f8)

<br>
<h4>Tekstlig beskrivelse av sekvensdiagrammet for use caset “Finn badeplasser”</h4> 
<br>
Dette use caset danner grunnlaget for use caset “se værvarsel for badeplass”, men representerer alene en grunnleggende funksjonalitet i applikasjonen vår - altså å finne badeplasser. 
<br>
Det skilles ut i et eget use case for å understreke funksjonaliteten appen vår har for å utforske ulike badeplasser og hvor de befinner seg hen, uten å nødvendigvis være interessert i å studere værvarslet for alle de ulike badeplassene.

<br>
Det er en naturlig betingelse for å kunne se værvarsel for et badested, og kunne derfor blitt slått sammen med use caset “se værvarsel for badeplass”, men vi velger altså å skille det ut som et eget use case for å understreke dens funksjon i seg selv, samt å gjøre det lettere å visualisere funksjonaliteten til appen. 

<br>

I tillegg er det verdt å nevne at det ved forsøk på å hente badeplasser et sted det ikke finnes badeplasser blir returnert en tom liste, og dermed ikke blir vist noe i kartet. I og med at dette ikke avviker fra normalflyten, som kan antas å medføre at listen med badesteder blir vist i kartet, velger heller ikke å inkludere en alternativ flyt da dette faktisk ikke er tilfelle – resultatet av hendelsesflyten er heller da at det vises en tom liste med badeplasser i kartet. 

<br>

1. Brukeren trykker på “Finn badeplasser”-knappen 

2. Systemet sender informasjon om lokasjonen oppover til datakilden for badeplasser. Informasjonen om lokasjon vises i diagrammet som “lat” for “latitude” og “lon” for “longitude. Lat og lon representerer et punkt midt i kartet når brukeren trykker på knappen. 

3. Systemet lager en liste av badeplassene innenfor en radius på 20 km fra punktet det mottar og sender listen til UI-laget. 

4. Systemet viser listen over badeplasser - dette kan også innebære å vise “den tomme listen” og ingen badesteder vil vises i kartet.

<br><br>

<h3> 6.2 Use case - se værvarsel for badeplass </h3>

![sekvd_seVærBad](https://media.github.uio.no/user/9349/files/a62350cd-ff30-47fb-9dd1-c7298b8b340c)

<br>
<b>Tekstlig beskrivelse av sekvensdiagrammet for use caset “Se værvarsel for badeplass”</b>
<br>
Dette use caset blir valgt å visualiseres her fordi dette use caset omhandler et av kravene for prosjektet - å hente og vise farevarsler, og er derfor et av de viktigste funksjonalitetene i applikasjonen. 
<br>

Det er også relevant å nevne at dette use caset forutsetter at badeplasser allerede er blitt søkt etter, funnet og nå vises i kartvisningen på enheten til brukeren. Vi velger å ikke inkludere denne funksjonaliteten i diagrammet fordi det står for seg selv som et eget use case, og det ville bidratt til å gjøre det mer uoversiktlig. 
<br>
I tillegg viser det heller ikke alternativ flyt for brukerinteraksjon i tilfellet hvor det vises farevarsel. Dette ville kun vært kommunikasjon fram og tilbake mellom bruker og UI-laget og visualiseres bedre i et aktivitetsdiagram (se aktivitetsdiagram over med tilhørene tekstlig beskrivelse). 

<br>

<b>Beskrivelse av flyt</b>
1. Brukeren trykker på et av badestedene som vises på skjermen 
2. Kartet sentreres på det valgte badestedet 
3. Composable-funksjonen initierer henting av værinformasjon og sender med lokasjonsinformasjon for det spesifikke badestedet. I diagrammet her eksemplifiseres det med forkortelsene ‘lat’ og ‘lon’ for henholdsvis “latitude” og “longitude”. 
I den faktiske kodeimplementasjonen er dette egentlig et Spot()-objekt med liknende informasjon, men unngås her, og videre i diagrammet, for å gjøre det mer forståelig for leseren hva slags informasjon som er involvert. 

4. Systemet bruker lokasjonsinformasjonen for å hente værinformasjon fra API-ene 
5. Systemet kombinerer relevant værinformasjon i et WeatherForecast()-objekt. 
6. Disse objektene kombineres i én liste som består av værinformasjon for hver time de 24 neste timene slik at man får et værvarsel for det neste døgnet. Her går systemet i loop (se 4) i diagrammet) for å hente værvarslet i de timesintervallene. 
7. Denne listen returneres og danner grunnlaget for en MutableStateFlow som sendes til UI-laget. 

8. Informasjon om farevarseler skjer på liknende måte. Dette eneste som skiller det fra henting av værinformasjon er at det for farevarsler ikke er nødvendig å hente farevarsel i timesintervaller de nester 24 timene, da det ikke finnes timesintervaller for farevarslene. 
        
9. Dersom det eksisterer farevarsler for den gitte lokasjonen, blir denne vist i skjermen sammen med det øvrige værvarslet. 
