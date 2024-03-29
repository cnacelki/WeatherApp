package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.R
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.HourlyActivity
import com.example.weatherapp.data.models.WeatherForecast
import com.example.weatherapp.data.models.WeatherMain
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.adapters.ForecastAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val apikey = "566f531109d265633437418ae7db5607"
    private var currWeather: WeatherMain? = null
    private var forecastWeather: WeatherForecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weather.collect {
                    when (it) {
                        is WeatherUIState.MainSuccess -> {
                            currWeather = it.weatherResult
                            setWeatherData()
                            getForecastData(
                                currWeather?.coord?.lat?.toInt().toString(),
                                currWeather?.coord?.lon?.toInt().toString()
                            )
                        }
                        is WeatherUIState.Error -> {
                            Toast.makeText(
                                applicationContext,
                                it.error.toString(),
                                Toast.LENGTH_LONG
                            )
                        }
                        is WeatherUIState.ForecastSuccess -> {
                            forecastWeather = it.forecastResult
                            setForecastData()
                        }
                        is WeatherUIState.ForecastError -> {
                            Toast.makeText(
                                applicationContext,
                                it.error.toString(),
                                Toast.LENGTH_LONG
                            )
                        }
                    }
                }
            }
        }
    }

    fun setWeather(desc: String?, img: ImageView?) {
        when (desc) {
            "Clear" -> img!!.setImageResource(R.drawable.sun)
            "Clouds" -> img!!.setImageResource(R.drawable.cloud)
            "Rain" -> img!!.setImageResource(R.drawable.rain)
            "Snow" -> img!!.setImageResource(R.drawable.snowfall)
        }
    }

    fun getCurrentWeather(view: View?) {
        val location = binding.cityText!!.text.toString().trim { it <= ' ' }
        viewModel.getCurrWeather(location, apikey)
    }

    fun getForecastData(lat: String, lon: String) {
        viewModel.getForecastWeather(lat, lon, apikey)
    }
    fun setForecastData() {
        val recyclerView = findViewById<RecyclerView>(R.id.forecastRecyclerView)
        recyclerView.adapter = forecastWeather?.let { ForecastAdapter(this, it.daily) }
        recyclerView.setHasFixedSize(true)
    }
    fun setWeatherData() {
        binding.resultText.text = "${currWeather?.main?.temp} °C"
        binding.resultWind.text = "${currWeather?.wind?.speed} m/s"
        setWeather(currWeather?.weather?.get(0)?.description, binding.resultImg)
    }
    fun secondActivity(view: View?) {
        val intent = Intent(this, HourlyActivity::class.java)
        intent.putExtra("Url", "AAAAAAA")
        startActivity(intent)
    }

}