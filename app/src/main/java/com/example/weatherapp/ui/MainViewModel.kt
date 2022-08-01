package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.WeatherForecast
import com.example.weatherapp.data.models.WeatherMain
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: WeatherRepository) : ViewModel() {
    private val _weather = MutableStateFlow<WeatherUIState>(WeatherUIState.Empty)
    val weather: StateFlow<WeatherUIState> = _weather

    fun getCurrWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            repository.getCurrentWeather(city, apiKey).collect { result ->
                when (result) {
                    is WeatherMain -> {
                        _weather.value = WeatherUIState.MainSuccess(result)
                    }
                    is Exception -> {
                        _weather.value = WeatherUIState.Error(result)
                    }
                }
            }
        }
    }

    fun getForecastWeather(lat: String, lon: String, apiKey: String) {
        viewModelScope.launch {
            repository.getForecastWeather(lat, lon, apiKey).collect { result ->
                when (result) {
                    is WeatherForecast -> {
                        _weather.value = WeatherUIState.ForecastSuccess(result)
                    }
                    is Exception -> {
                        _weather.value = WeatherUIState.Error(result)
                    }
                }
            }
        }
    }
}

sealed class WeatherUIState {
    data class MainSuccess(var weatherResult: WeatherMain?) : WeatherUIState()
    data class ForecastSuccess(var forecastResult: WeatherForecast?) : WeatherUIState()
    data class Error(var error: Exception) : WeatherUIState()
    object Empty : WeatherUIState()
}