package com.example.android.connectedweather.data

import com.squareup.moshi.Json
import java.io.FileDescriptor
import java.io.Serializable

data class Main (
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int
) : Serializable

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Serializable

data class Clouds(
    val all: Int
) : Serializable

data class Wind(
    val speed: Float,
    val deg: Int,
    val gust: Float
) : Serializable

data class Rain(
    @Json(name="3h") val vol: Float
) : Serializable

data class Snow(
    @Json(name="3h") val vol: Float
) : Serializable

data class Sys(
    val pod: String
) : Serializable

data class WeatherResults(
    val dt: Int,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds?,
    val wind: Wind?,
    val visibility: Int,
    val pop: Float,
    val rain: Rain?,
    val snow: Snow?,
    val sys: Sys,
    val dt_txt: String
) : Serializable

// Given
data class ForecastPeriod(
    val year: Int,
    val month: Int,
    val day: Int,
    val highTemp: Int,
    val lowTemp: Int,
    val pop: Double,
    val shortDesc: String,
    val longDesc: String
)