package com.example.checkmate.model

data class WeatherResponse(
    val list: List<ForecastEntry>
)

data class ForecastEntry(
    val dt_txt: String,  // 예: "2024-05-26 15:00:00"
    val main: MainInfo,
    val weather: List<WeatherInfo>
)

data class MainInfo(
    val temp: Double
)

data class WeatherInfo(
    val description: String,
    val icon: String
)
