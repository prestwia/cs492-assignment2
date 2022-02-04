package com.example.android.connectedweather.data

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
