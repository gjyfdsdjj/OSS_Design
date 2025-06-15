data class ForecastEntry(
    val dt_txt: String, // "2024-05-25 12:00:00"
    val main: MainInfo,
    val weather: List<WeatherInfo>
)

data class MainInfo(val temp: Double)
data class WeatherInfo(val description: String, val icon: String)


