package com.example.weatherapp.di

import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.services.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val BASE_URL="https://api.openweathermap.org/data/2.5/"
    @Provides
    @Singleton
    fun provideRetrofit(): WeatherService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(WeatherService::class.java)
    }
    @Provides
    @Singleton
    fun provideWeatherRepository(service: WeatherService): WeatherRepository {
        return WeatherRepository(service)
    }
}