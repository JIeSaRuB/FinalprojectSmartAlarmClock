package com.example.finalprojectsmartalarmclock

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testGetCurrentDate() {
        val fragment = MainFragment()
        val currentDate = fragment.getCurrentDate()

        //Проверка непустого значения даты и времени
        assertNotNull(currentDate)

        //Проверка формата даты и времени
        assertTrue(currentDate.matches(Regex("\\d{2}:\\d{2}:\\d{2}\n")))
    }
    @Test
    fun testWeatherCodeToWeather() {
        val fragment = MainFragment()

        //Проверка кодов погоды
        assertEquals("Ясно", fragment.weatherCodeToWeather(0))
        assertEquals("Переменная облачность", fragment.weatherCodeToWeather(1))
        assertEquals("Переменная облачность", fragment.weatherCodeToWeather(2))
        assertEquals("Туман", fragment.weatherCodeToWeather(45))
        assertEquals("Облачно", fragment.weatherCodeToWeather(100))
    }
    @Test
    fun testRequestLocation() {
        val fragment = MainFragment()

        // Проверяем, что метод requestLocation() вызывается без ошибок
        fragment.requestLocation()
    }







}