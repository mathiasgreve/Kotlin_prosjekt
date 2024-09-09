package no.uio.ifi.in2000.team32.prosjekt.model.locationforecast

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class GetWeatherIconFileNameTest {

    @Test
    fun testPartlyCloudyDayEnglish() {
        val description = "partlycloudy_day"
        val expected = "03d.png"
        val result = getWeatherIconFileName(description)
        assertEquals(expected, result, "Feil i ikonfilen for partly cloudy day")
    }

    @Test
    fun testPartlyCloudyDayBokmal() {
        val description = "klarvær_night"
        val expected = "01n.png"
        val result = getWeatherIconFileName(description)
        assertEquals(expected, result, "Feil i ikonfilen for partly cloudy day")
    }

    @Test
    fun testPartlyCloudyDayNynorsk() {
        val description = "klårvêr_night"
        val expected = "01n.png"
        val result = getWeatherIconFileName(description)
        assertEquals(expected, result, "Feil i ikonfilen for partly cloudy day")
    }

    @Test
    fun testPartlyCloudyNight() {
        val description = "partly      cloudy_night"
        val expected = "03n.png"
        assertEquals(
            expected,
            getWeatherIconFileName(description),
            "Feil i ikonfilen for partly cloudy night"
        )
    }

    @Test
    fun testClearSkyDay() {
        val description = "clearsky_day"
        val expected = "01d.png"
        assertEquals(
            expected,
            getWeatherIconFileName(description),
            "Feil i ikonfilen for clear sky day"
        )
    }

    @Test
    fun testFogNight() {
        val description = "fog_night"
        val expected = "15n.png"
        assertEquals(
            expected,
            getWeatherIconFileName(description),
            "Feil i ikonfilen for fog night"
        )
    }

    @Test
    fun testUnknownDescription() {
        val description = "TORDNADO_meteornedslag"
        val expected = "unknown.png"
        assertEquals(
            expected,
            getWeatherIconFileName(description),
            "Feil i ikonfilen for ukjent værtype"
        )
    }

    @Test
    fun testUnknownDescriptionWrong() {
        val description = "sunny_PARTY_weather_HOT_DADDY!!!!"
        val incorrect = "01d.png"
        assertNotEquals(
            incorrect,
            getWeatherIconFileName(description),
            "Feil: Funksjonen returnerte et kjent ikon for en ukjent værtype"
        )
    }

    @Test
    fun testWhiteSpaceRemover() {
        val description = "klarvær              night"
        val expected = "01n.png"
        val result = getWeatherIconFileName(description)
        assertEquals(expected, result, "Feil i ikonfilen for partly cloudy day")
    }

    @Test
    fun testUnderScoreRemover() {
        val description = "klarvær____night"
        val expected = "01n.png"
        val result = getWeatherIconFileName(description)
        assertEquals(expected, result, "Feil i ikonfilen for partly cloudy day")
    }
}