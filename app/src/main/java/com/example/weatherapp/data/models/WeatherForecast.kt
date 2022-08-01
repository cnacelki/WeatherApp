package com.example.weatherapp.data.models

data class WeatherForecast(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Int,
    val lon: Int,
    val minutely: List<Minutely>,
    val timezone: String,
    val timezone_offset: Int
)