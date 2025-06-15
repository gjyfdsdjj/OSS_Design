package com.example.checkmate.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.YearMonth

@Composable
fun CalendarHeader(
    calendarState: CalendarState,
    coroutineScope: CoroutineScope
) {
    val visibleMonth = calendarState.firstVisibleMonth.yearMonth

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            coroutineScope.launch {
                val prevMonth = visibleMonth.minusMonths(1)
                calendarState.animateScrollToMonth(prevMonth)
            }
        }) {
            Text("◀")
        }

        Text(
            text = "${visibleMonth.year}년 ${visibleMonth.monthValue}월",
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = {
            coroutineScope.launch {
                val nextMonth = visibleMonth.plusMonths(1)
                calendarState.animateScrollToMonth(nextMonth)
            }
        }) {
            Text("▶")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}
