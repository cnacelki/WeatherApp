package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.WeatherMain
import com.example.weatherapp.data.services.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val service: WeatherService) {

    suspend fun getCurrentWeather(city: String, apiKey: String) = flow {
        try {
            val response = service.getCurrWeather(city, apiKey)
            emit(response)
        } catch (e: Exception) {
            emit(e)
        }
    }

    suspend fun getForecastWeather(lat: String, lon: String, apiKey: String) = flow {
        try {
            val response = service.getForecastWeather(lat, lon, apiKey)
            emit(response)
        } catch (e: Exception) {
            emit(e)
        }
    }
}