package com.example.weatherapp.data.services

import com.example.weatherapp.data.models.WeatherForecast
import com.example.weatherapp.data.models.WeatherMain
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getCurrWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ):WeatherMain

    @GET("onecall")
    suspend fun getForecastWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
    ):WeatherForecast
}