package com.example.checkmate.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.checkmate.model.ForecastEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.util.Log

@Composable
fun WeatherSummary(
    forecasts: List<ForecastEntry>,
    selectedDate: LocalDate
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateStr = selectedDate.format(formatter)
    val match = forecasts.find {
        Log.d("WEATHER_SUMMARY", "dt_txt: ${it.dt_txt}, selected: $dateStr")
        it.dt_txt.startsWith(dateStr)
    }

    if (match != null) {
        val iconCode = match.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color(0xFFE0F7FA),
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 1.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(iconUrl),
                    contentDescription = "날씨 아이콘",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = match.weather.firstOrNull()?.description ?: "날씨 정보 없음",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${match.main.temp}°C",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    } else {
        Text(
            text = "날씨 정보 없음",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
