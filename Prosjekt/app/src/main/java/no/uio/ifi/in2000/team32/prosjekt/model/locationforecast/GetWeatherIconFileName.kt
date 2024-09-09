package no.uio.ifi.in2000.team32.prosjekt.model.locationforecast

/******************** Kildehenvisning ****************************

## Bruk av ikoner.

NRK tillater fri bruk av disse ikonene mot kreditering.
Værikonene brukt i dette programmet er hentet fra NRK og yr.no.

## Bruk av AI
Denne metoden er i stor grad generert av ChatGPT ettersom den gjør svært mye repetetivt arbeid.

#Kilder:
NRK. (u å). Yr weather symbols. Hentet April 2024 fra NRK - GitHub:
https://nrkno.github.io/yr-weather-symbols/

ChatGPT. (2024, April). ChatGPT.
Hentet fra GhatGPT: https://chatgpt.com/

 **********************************************************************/

fun getWeatherIconFileName(description: String): String {
    val normalizedDescription = description
        .lowercase()
        .replace("_", "")
        .replace(Regex("\\s+"), "")
        .trim()

    val suffix = when {
        normalizedDescription.endsWith("day") -> "d"
        normalizedDescription.endsWith("night") -> "n"
        normalizedDescription.endsWith("polartwilight") -> "m"
        else -> ""
    }

    val weatherDescriptionToFileName = mapOf(

        //Nynorsk mappings
        "klårvêr" to "01",
        "lettskya" to "02",
        "delvisskya" to "03",
        "skya" to "04",
        "letteregnbyer" to "40",
        "regnbyer" to "05",
        "kraftigeregnbyer" to "41",
        "letteregnbyerogtorevêr" to "24",
        "regnbyerogtorevêr" to "06",
        "kraftigeregnbyerogtorevêr" to "25",
        "lettesluddbyer" to "42",
        "sluddbyer" to "07",
        "kraftigesluddbyer" to "43",
        "lettesluddbyerogtorevêr" to "26",
        "sluddbyerogtorevêr" to "20",
        "kraftigesluddbyerogtorevêr" to "27",
        "lettesnøbyer" to "44",
        "snøbyer" to "08",
        "kraftigesnøbyer" to "45",
        "lettesnøbyerogtorevêr" to "28",
        "snøbyerogtorevêr" to "21",
        "kraftigesnøbyerogtorevêr" to "29",
        "lettreng" to "46",
        "regn" to "09",
        "kraftigregn" to "10",
        "lettrengogtorevêr" to "30",
        "regnogtorevêr" to "22",
        "kraftigregnogtorevêr" to "11",
        "lettsludd" to "47",
        "sludd" to "12",
        "kraftigsludd" to "48",
        "lettsluddogtorevêr" to "31",
        "sluddogtorevêr" to "23",
        "kraftigsluddogtorevêr" to "32",
        "lettsnø" to "49",
        "snø" to "13",
        "kraftigsnø" to "50",
        "lettsnøogtorevêr" to "33",
        "snøogtorevêr" to "14",
        "kraftigsnøogtorevêr" to "34",
        "skodde" to "15",

        //Bokmål mappings
        "klarvær" to "01",
        "lettskyet" to "02",
        "delvisskyet" to "03",
        "skyet" to "04",
        "letteregnbyger" to "40",
        "regnbyger" to "05",
        "kraftigeregnbyger" to "41",
        "letteregnbygerogtorden" to "24",
        "regnbygerogtorden" to "06",
        "kraftigeregnbygerogtorden" to "25",
        "lettesluddbyger" to "42",
        "sluddbyger" to "07",
        "kraftigesluddbyger" to "43",
        "lettesluddbygerogtorden" to "26",
        "sluddbygerogtorden" to "20",
        "kraftigesluddbygerogtorden" to "27",
        "lettesnøbyger" to "44",
        "snøbyger" to "08",
        "kraftigesnøbyger" to "45",
        "lettesnøbygerogtorden" to "28",
        "snøbygerogtorden" to "21",
        "kraftigesnøbygerogtorden" to "29",
        "lettreng" to "46",
        "regn" to "09",
        "kraftigregn" to "10",
        "lettrengogtorden" to "30",
        "regnogtorden" to "22",
        "kraftigregnogtorden" to "11",
        "lettsludd" to "47",
        "sludd" to "12",
        "kraftigsludd" to "48",
        "lettsluddogtorden" to "31",
        "sluddogtorden" to "23",
        "kraftigsluddogtorden" to "32",
        "lettsnø" to "49",
        "snø" to "13",
        "kraftigsnø" to "50",
        "lettsnøogtorden" to "33",
        "snøogtorden" to "14",
        "kraftigsnøogtorden" to "34",
        "tåke" to "15",

        //Engelsk mappings
        "clearsky" to "01",
        "fair" to "02",
        "partlycloudy" to "03",
        "cloudy" to "04",
        "lightrainshowers" to "40",
        "rainshowers" to "05",
        "heavyrainshowers" to "41",
        "lightrainshowersandthunder" to "24",
        "rainshowersandthunder" to "06",
        "heavyrainshowersandthunder" to "25",
        "lightsleetshowers" to "42",
        "sleetshowers" to "07",
        "heavysleetshowers" to "43",
        "lightsleetshowersandthunder" to "26",
        "sleetshowersandthunder" to "20",
        "heavysleetshowersandthunder" to "27",
        "lightsnowshowers" to "44",
        "snowshowers" to "08",
        "heavysnowshowers" to "45",
        "lightsnowshowersandthunder" to "28",
        "snowshowersandthunder" to "21",
        "heavysnowshowersandthunder" to "29",
        "lightrain" to "46",
        "rain" to "09",
        "heavyrain" to "10",
        "lightrainandthunder" to "30",
        "rainandthunder" to "22",
        "heavyrainandthunder" to "11",
        "lightsleet" to "47",
        "sleet" to "12",
        "heavysleet" to "48",
        "lightsleetandthunder" to "31",
        "sleetandthunder" to "23",
        "heavysleetandthunder" to "32",
        "lightsnow" to "49",
        "snow" to "13",
        "heavysnow" to "50",
        "lightsnowandthunder" to "33",
        "snowandthunder" to "14",
        "heavysnowandthunder" to "34",
        "fog" to "15"

    )


    val iconCode =
        weatherDescriptionToFileName[normalizedDescription.removeSuffix("day").removeSuffix("night")
            .removeSuffix("polartwilight")] ?: "unknown"
    val fileName = if (suffix.isNotEmpty()) "$iconCode$suffix.png" else "$iconCode.png"
    return fileName  // Returnerer "unknown" hvis ikke funnet
}

